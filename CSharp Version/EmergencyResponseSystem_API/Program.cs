using EmergencyResponseSystem_DAL.Database;
using EmergencyResponseSystem_DAL.Services;
using EmergencyResponseSystem_DAL.Settings;
using Logger;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle


builder.Services.AddScoped<SpecialisationService>();
builder.Services.AddScoped<HopitalSpecialisationService>();
builder.Services.AddScoped<HopitalService>();
builder.Services.AddScoped<PatientService>();

// Pour autoriser les requêtes CORS de même origine
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll",
        builder => builder.AllowAnyOrigin()
                          .AllowAnyMethod()
                          .AllowAnyHeader());
});


builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// A utiliser dans le cadre de tests uniquement
app.UseCors("AllowAll");

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

LogsManager.Verbose = Verbose.Trace;
if (!File.Exists(SettingsManager.Instance.GetDatasource() + ".db"))
{
    DBManager.InitDB();
    await DBManager.CreerDataset();
}

app.Run();
