using System;
using System.Collections.Generic;

namespace pret_management.Models;

public partial class HistoriqueStatutPret
{
    public int Id { get; set; }

    public int IdPret { get; set; }

    public int IdStatut { get; set; }

    public DateTime DateModification { get; set; }

    public virtual Pret IdPretNavigation { get; set; } = null!;

    public virtual StatutPret IdStatutNavigation { get; set; } = null!;
}
