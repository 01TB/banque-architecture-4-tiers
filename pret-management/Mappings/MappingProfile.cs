using AutoMapper;
using pret_management.DTOs;
using pret_management.Models;

namespace pret_management.Mappings;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        // Mapping existants...
        CreateMap<Pret, PretDto>();
        CreateMap<CreatePretDto, Pret>();
        CreateMap<UpdatePretDto, Pret>()
            .ForAllMembers(opts => opts.Condition((src, dest, srcMember) => srcMember != null));

        CreateMap<Client, ClientDto>();
        
        // Nouveaux mappings pour Remboursement et Mouvement
        CreateMap<RemboursementPret, RemboursementPretDto>();
        CreateMap<CreateRemboursementDto, RemboursementPret>();
        
        CreateMap<MouvementCompteCourant, MouvementCompteCourantDto>();
        CreateMap<CreateMouvementCompteCourantDto, MouvementCompteCourant>();
    }
}