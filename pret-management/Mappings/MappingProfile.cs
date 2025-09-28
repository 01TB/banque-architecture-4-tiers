using AutoMapper;
using pret_management.DTOs;
using pret_management.Models;

namespace pret_management.Mappings;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        // Mapping pour Pret
        CreateMap<Pret, PretDto>();
        CreateMap<CreatePretDto, Pret>();
        CreateMap<UpdatePretDto, Pret>()
            .ForAllMembers(opts => opts.Condition((src, dest, srcMember) => srcMember != null));

        // Mapping pour Client
        CreateMap<Client, ClientDto>();

        // Mapping pour Remboursement
        CreateMap<RemboursementPret, RemboursementPretDto>();
        CreateMap<CreateRemboursementDto, RemboursementPret>();
    }
}