namespace pret_management.DTOs;

public class ClientDto
{
    public int Id { get; set; }
    public string Matricule { get; set; } = null!;
    public string Nom { get; set; } = null!;
    public string Prenom { get; set; } = null!;
    public DateOnly DateNaissance { get; set; }
    public string Email { get; set; } = null!;
    public string Telephone { get; set; } = null!;
}