using System.ComponentModel.DataAnnotations;

namespace pret_management.DTOs;

public class RemboursementPretDto
{
    public int Id { get; set; }
    public decimal MontantRembourse { get; set; }
    public decimal InteretPaye { get; set; }
    public DateTime DatePaiement { get; set; }
    public int IdPret { get; set; }
}

public class CreateRemboursementDto
{
    [Required(ErrorMessage = "L'ID du prêt est requis")]
    public int IdPret { get; set; }
    
    [Required(ErrorMessage = "Le montant remboursé est requis")]
    [Range(0.01, double.MaxValue, ErrorMessage = "Le montant doit être supérieur à 0")]
    public decimal MontantRembourse { get; set; }
    
    [Required(ErrorMessage = "L'intérêt payé est requis")]
    [Range(0, double.MaxValue, ErrorMessage = "L'intérêt doit être positif")]
    public decimal InteretPaye { get; set; }
    
    [Required(ErrorMessage = "La date de paiement est requise")]
    public DateTime DatePaiement { get; set; }
}