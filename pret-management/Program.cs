using Microsoft.EntityFrameworkCore;
using pret_management.Data;
using pret_management.Mappings; // Adaptez le namespace selon votre projet

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
// Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
builder.Services.AddOpenApi();

// ✅ Configuration de la base de données
builder.Services.AddDbContext<BanqueDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

// Configuration d'AutoMapper
builder.Services.AddAutoMapper(typeof(MappingProfile));

// Configuration de la gestion des erreurs
builder.Services.AddProblemDetails();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.UseHttpsRedirection();

// ✅ Ajoutez le routing et mappez les contrôleurs
app.UseRouting();
app.MapControllers();

app.Run();