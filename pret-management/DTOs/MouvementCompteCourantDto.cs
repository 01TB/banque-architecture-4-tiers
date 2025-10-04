using System.ComponentModel.DataAnnotations;

namespace pret_management.DTOs;

public class MouvementCompteCourantDto
{
    public int Id { get; set; }
    public decimal Montant { get; set; }
    public string Description { get; set; } = null!;
    public DateTime DateMouvement { get; set; }
    public int IdTypeMouvement { get; set; }
    public int IdCompteCourant { get; set; }
}

public class CreateMouvementCompteCourantDto
{
    [Required(ErrorMessage = "Le montant est requis")]
    public decimal Montant { get; set; }
    
    [Required(ErrorMessage = "La description est requise")]
    public string Description { get; set; } = null!;
    
    [Required(ErrorMessage = "L'ID du type de mouvement est requis")]
    public int IdTypeMouvement { get; set; }
    
    [Required(ErrorMessage = "L'ID du compte courant est requis")]
    public int IdCompteCourant { get; set; }
}