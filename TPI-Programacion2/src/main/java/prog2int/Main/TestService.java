package prog2int.Main;

import prog2int.Models.*;
import prog2int.Service.EmpleadoService;

import java.time.LocalDate;

public class TestService {
    public static void main(String[] args) {
        try {
            EmpleadoService service = new EmpleadoService();

            Legajo legajo = new Legajo(null, false, "ADM-004", "Administrativo",
                    EstadoLegajo.ACTIVO, LocalDate.now(), "Legajo creado desde Service");
            Empleado emp = new Empleado(null, false, "Lucía", "Gómez", "40987654",
                    "lucia.gomez@empresa.com", LocalDate.now(), "Recursos Humanos", legajo);

            service.crear(emp);
            System.out.println("✅ Empleado + Legajo creados exitosamente!");

            System.out.println("\n📋 Listado de empleados:");
            service.leerTodos().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
