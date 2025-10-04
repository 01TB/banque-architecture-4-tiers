using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using AutoMapper;
using pret_management.Data;
using pret_management.DTOs;
using pret_management.Models;

namespace pret_management.Controllers;

[ApiController]
[Route("api/[controller]")]
public class PretsController : ControllerBase
{
    private readonly BanqueDbContext _context;
    private readonly IMapper _mapper;
    private readonly ILogger<PretsController> _logger;

    public PretsController(BanqueDbContext context, IMapper mapper, ILogger<PretsController> logger)
    {
        _context = context;
        _mapper = mapper;
        _logger = logger;
    }

    // GET: api/Prets
    [HttpGet]
    public async Task<ActionResult<IEnumerable<PretDto>>> GetPrets()
    {
        try
        {
            var prets = await _context.Prets
                .Include(p => p.IdClientNavigation)
                .Include(p => p.RemboursementPrets)
                .ToListAsync();

            var pretDtos = _mapper.Map<List<PretDto>>(prets);
            return Ok(pretDtos);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Erreur lors de la récupération des prêts");
            return StatusCode(500, "Une erreur interne est survenue");
        }
    }

    // GET: api/Prets/5
    [HttpGet("{id}")]
    public async Task<ActionResult<PretDto>> GetPret(int id)
    {
        try
        {
            var pret = await _context.Prets
                .Include(p => p.IdClientNavigation)
                .Include(p => p.RemboursementPrets)
                .FirstOrDefaultAsync(p => p.Id == id);

            if (pret == null)
            {
                return NotFound($"Prêt avec l'ID {id} non trouvé");
            }

            var pretDto = _mapper.Map<PretDto>(pret);
            return Ok(pretDto);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Erreur lors de la récupération du prêt {Id}", id);
            return StatusCode(500, "Une erreur interne est survenue");
        }
    }

    // POST: api/Prets
    [HttpPost]
    public async Task<ActionResult<PretDto>> CreatePret(CreatePretDto createPretDto)
    {
        try
        {
            // Validation supplémentaire
            var clientExists = await _context.Clients.AnyAsync(c => c.Id == createPretDto.IdClient);
            if (!clientExists)
            {
                return BadRequest($"Le client avec l'ID {createPretDto.IdClient} n'existe pas");
            }

            var pret = _mapper.Map<Pret>(createPretDto);
            pret.DateCreation = DateTime.UtcNow;

            _context.Prets.Add(pret);
            await _context.SaveChangesAsync();

            var pretDto = _mapper.Map<PretDto>(pret);
            return CreatedAtAction(nameof(GetPret), new { id = pret.Id }, pretDto);
        }
        catch (DbUpdateException ex)
        {
            _logger.LogError(ex, "Erreur de base de données lors de la création du prêt");
            return StatusCode(500, "Erreur lors de la création du prêt");
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Erreur lors de la création du prêt");
            return StatusCode(500, "Une erreur interne est survenue");
        }
    }

    // PUT: api/Prets/5
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdatePret(int id, UpdatePretDto updatePretDto)
    {
        try
        {
            var pret = await _context.Prets.FindAsync(id);
            if (pret == null)
            {
                return NotFound($"Prêt avec l'ID {id} non trouvé");
            }

            // Vérifier si le prêt n'est pas déjà fermé
            if (pret.DateFermeture.HasValue)
            {
                return BadRequest("Impossible de modifier un prêt déjà fermé");
            }

            _mapper.Map(updatePretDto, pret);
            
            await _context.SaveChangesAsync();

            return NoContent();
        }
        catch (DbUpdateConcurrencyException ex)
        {
            if (!await PretExists(id))
            {
                return NotFound();
            }
            _logger.LogError(ex, "Erreur de concurrence lors de la mise à jour du prêt {Id}", id);
            return StatusCode(500, "Erreur de concurrence lors de la mise à jour");
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Erreur lors de la mise à jour du prêt {Id}", id);
            return StatusCode(500, "Une erreur interne est survenue");
        }
    }

    // DELETE: api/Prets/5
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeletePret(int id)
    {
        try
        {
            var pret = await _context.Prets.FindAsync(id);
            if (pret == null)
            {
                return NotFound($"Prêt avec l'ID {id} non trouvé");
            }

            // Vérifier s'il y a des remboursements associés
            var hasRemboursements = await _context.RemboursementPrets
                .AnyAsync(r => r.IdPret == id);
            
            if (hasRemboursements)
            {
                return BadRequest("Impossible de supprimer un prêt avec des remboursements associés");
            }

            _context.Prets.Remove(pret);
            await _context.SaveChangesAsync();

            return NoContent();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Erreur lors de la suppression du prêt {Id}", id);
            return StatusCode(500, "Une erreur interne est survenue");
        }
    }

    // GET: api/Prets/5/Remboursements
    [HttpGet("{id}/remboursements")]
    public async Task<ActionResult<IEnumerable<RemboursementPretDto>>> GetRemboursements(int id)
    {
        try
        {
            var pretExists = await PretExists(id);
            if (!pretExists)
            {
                return NotFound($"Prêt avec l'ID {id} non trouvé");
            }

            var remboursements = await _context.RemboursementPrets
                .Include(r => r.IdMouvementCompteCourantNavigation)
                .Where(r => r.IdPret == id)
                .ToListAsync();

            var remboursementDtos = _mapper.Map<List<RemboursementPretDto>>(remboursements);
            return Ok(remboursementDtos);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Erreur lors de la récupération des remboursements du prêt {Id}", id);
            return StatusCode(500, "Une erreur interne est survenue");
        }
    }
    
    private async Task<bool> VerifierSoldeSuffisant(int idCompteCourant, decimal montant)
    {
        var compte = await _context.CompteCourants.FindAsync(idCompteCourant);
        return compte != null && compte.Solde >= montant;
    }

    // POST: api/Prets/5/Remboursements
    [HttpPost("{id}/remboursements")]
    public async Task<ActionResult<RemboursementPretDto>> CreateRemboursement(int id, CreateRemboursementDto createRemboursementDto)
    {
        using var transaction = await _context.Database.BeginTransactionAsync();
        
        try
        {
            if (id != createRemboursementDto.IdPret)
            {
                return BadRequest("L'ID du prêt dans l'URL ne correspond pas à l'ID dans le corps de la requête");
            }

            // Vérifier que le prêt existe et n'est pas fermé
            var pret = await _context.Prets
                .Include(p => p.IdClientNavigation)
                .ThenInclude(c => c.CompteCourant)
                .FirstOrDefaultAsync(p => p.Id == id);

            if (pret == null)
            {
                return NotFound($"Prêt avec l'ID {id} non trouvé");
            }

            if (pret.DateFermeture.HasValue)
            {
                return BadRequest("Impossible d'ajouter un remboursement à un prêt fermé");
            }

            // Vérifier que le client a un compte courant
            if (pret.IdClientNavigation.CompteCourant == null)
            {
                return BadRequest($"Le client {pret.IdClientNavigation.Nom} {pret.IdClientNavigation.Prenom} n'a pas de compte courant");
            }

            // Trouver le type de mouvement "Débit" (supposons que l'ID 1 = Crédit, 2 = Débit)
            var typeMouvementDebit = await _context.TypeMouvementCompteCourants
                .FirstOrDefaultAsync(t => t.Id == 2);

            if (typeMouvementDebit == null)
            {
                return StatusCode(500, "Le type de mouvement 'Débit' n'est pas configuré dans le système");
            }

            // Calculer le montant total du remboursement
            var montantTotal = createRemboursementDto.MontantRembourse + createRemboursementDto.InteretPaye;
            
            // Avant de créer le mouvement, vérifier le solde
            var soldeSuffisant = await VerifierSoldeSuffisant(
                pret.IdClientNavigation.CompteCourant.Id, 
                montantTotal
            );

            if (!soldeSuffisant)
            {
                return BadRequest($"Solde insuffisant sur le compte courant pour effectuer le remboursement de {montantTotal:C}");
            }

            // Créer le mouvement de compte courant
            var mouvement = new MouvementCompteCourant
            {
                IdCompteCourant = pret.IdClientNavigation.CompteCourant.Id,
                IdTypeMouvement = typeMouvementDebit.Id,
                Montant = montantTotal,
                DateMouvement = createRemboursementDto.DatePaiement,
                Description = createRemboursementDto.Description ?? 
                             $"Remboursement prêt #{pret.Id} - Capital: {createRemboursementDto.MontantRembourse:C}, Intérêts: {createRemboursementDto.InteretPaye:C}"
            };

            _context.MouvementCompteCourants.Add(mouvement);
            await _context.SaveChangesAsync(); // Sauvegarder pour obtenir l'ID du mouvement

            // Créer le remboursement lié au mouvement
            var remboursement = new RemboursementPret
            {
                IdPret = createRemboursementDto.IdPret,
                IdMouvementCompteCourant = mouvement.Id,
                MontantRembourse = createRemboursementDto.MontantRembourse,
                InteretPaye = createRemboursementDto.InteretPaye,
                DatePaiement = createRemboursementDto.DatePaiement
            };

            _context.RemboursementPrets.Add(remboursement);
            await _context.SaveChangesAsync();

            // Mettre à jour le solde du compte courant
            var compteCourant = pret.IdClientNavigation.CompteCourant;
            compteCourant.Solde -= montantTotal; // Débit du compte
            await _context.SaveChangesAsync();

            await transaction.CommitAsync();

            // Charger les données liées pour la réponse
            await _context.Entry(remboursement)
                .Reference(r => r.IdMouvementCompteCourantNavigation)
                .LoadAsync();

            var remboursementDto = _mapper.Map<RemboursementPretDto>(remboursement);
            return CreatedAtAction(nameof(GetRemboursements), new { id = pret.Id }, remboursementDto);
        }
        catch (Exception ex)
        {
            await transaction.RollbackAsync();
            _logger.LogError(ex, "Erreur lors de la création du remboursement pour le prêt {Id}", id);
            return StatusCode(500, "Une erreur interne est survenue lors de la création du remboursement");
        }
    }
    
    // GET: api/Prets/client/5
    [HttpGet("client/{idClient}")]
    public async Task<ActionResult<IEnumerable<PretDto>>> GetPretsByClient(int idClient)
    {
        try
        {
            // Vérifier si le client existe
            var clientExists = await _context.Clients.AnyAsync(c => c.Id == idClient);
            if (!clientExists)
            {
                return NotFound($"Client avec l'ID {idClient} non trouvé");
            }

            var prets = await _context.Prets
                .Include(p => p.IdClientNavigation)
                .Include(p => p.RemboursementPrets)
                .Where(p => p.IdClient == idClient)
                .OrderByDescending(p => p.DateCreation)
                .ToListAsync();

            if (!prets.Any())
            {
                return Ok(new List<PretDto>()); // Retourne une liste vide si aucun prêt
            }

            var pretDtos = _mapper.Map<List<PretDto>>(prets);
            return Ok(pretDtos);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Erreur lors de la récupération des prêts du client {IdClient}", idClient);
            return StatusCode(500, "Une erreur interne est survenue");
        }
    }

    private async Task<bool> PretExists(int id)
    {
        return await _context.Prets.AnyAsync(e => e.Id == id);
    }
}