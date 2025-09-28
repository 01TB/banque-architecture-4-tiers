using System.ComponentModel.DataAnnotations;

namespace pret_management.DTOs;

public class PretDto
{
    public int Id { get; set; }
    
    [Required(ErrorMessage = "L'ID client est requis")]
    public int IdClient { get; set; }
    
    [Required(ErrorMessage = "Le montant du prêt est requis")]
    [Range(0.01, double.MaxValue, ErrorMessage = "Le montant doit être supérieur à 0")]
    public decimal MontantPret { get; set; }
    
    [Required(ErrorMessage = "La périodicité de remboursement est requise")]
    [Range(1, int.MaxValue, ErrorMessage = "La périodicité doit être supérieure à 0")]
    public int PeriodiciteRemboursement { get; set; }
    
    [Required(ErrorMessage = "Le taux d'intérêt annuel est requis")]
    [Range(0, 100, ErrorMessage = "Le taux d'intérêt doit être entre 0 et 100")]
    public decimal TauxInteretAnnuel { get; set; }
    
    public DateTime DateCreation { get; set; }
    public DateTime? DateFermeture { get; set; }
    
    // Navigation properties pour les données liées
    public ClientDto? Client { get; set; }
    public List<RemboursementPretDto>? Remboursements { get; set; }
}

public class CreatePretDto
{
    [Required(ErrorMessage = "L'ID client est requis")]
    public int IdClient { get; set; }
    
    [Required(ErrorMessage = "Le montant du prêt est requis")]
    [Range(0.01, double.MaxValue, ErrorMessage = "Le montant doit être supérieur à 0")]
    public decimal MontantPret { get; set; }
    
    [Required(ErrorMessage = "La périodicité de remboursement est requise")]
    [Range(1, 360, ErrorMessage = "La périodicité doit être entre 1 et 360 mois")]
    public int PeriodiciteRemboursement { get; set; }
    
    [Required(ErrorMessage = "Le taux d'intérêt annuel est requis")]
    [Range(0, 100, ErrorMessage = "Le taux d'intérêt doit être entre 0 et 100")]
    public decimal TauxInteretAnnuel { get; set; }
}

public class UpdatePretDto
{
    [Range(0.01, double.MaxValue, ErrorMessage = "Le montant doit être supérieur à 0")]
    public decimal? MontantPret { get; set; }
    
    [Range(1, 360, ErrorMessage = "La périodicité doit être entre 1 et 360 mois")]
    public int? PeriodiciteRemboursement { get; set; }
    
    [Range(0, 100, ErrorMessage = "Le taux d'intérêt doit être entre 0 et 100")]
    public decimal? TauxInteretAnnuel { get; set; }
    
    public DateTime? DateFermeture { get; set; }
}