using System;
using System.Collections.Generic;

namespace pret_management.Models;

public partial class StatutCompte
{
    public int Id { get; set; }

    public string Libelle { get; set; } = null!;

    public virtual ICollection<CompteCourant> CompteCourants { get; set; } = new List<CompteCourant>();

    public virtual ICollection<CompteDepot> CompteDepots { get; set; } = new List<CompteDepot>();
}
