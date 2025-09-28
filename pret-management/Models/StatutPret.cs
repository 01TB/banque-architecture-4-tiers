using System;
using System.Collections.Generic;

namespace pret_management.Models;

public partial class StatutPret
{
    public int Id { get; set; }

    public string? Libelle { get; set; }

    public virtual ICollection<HistoriqueStatutPret> HistoriqueStatutPrets { get; set; } = new List<HistoriqueStatutPret>();
}
