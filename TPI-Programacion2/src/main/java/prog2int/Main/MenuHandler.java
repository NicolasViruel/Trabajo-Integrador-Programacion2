package prog2int.Main;

import prog2int.Models.*;
import prog2int.Service.EmpleadoService;
import prog2int.Service.LegajoService;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuHandler {
    private final Scanner scanner;
    private final EmpleadoService empleadoService = new EmpleadoService();
    private final LegajoService legajoService = new LegajoService();

    public MenuHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void crearEmpleado() {
        try {
            System.out.println("\n--- Crear Empleado ---");
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();
            System.out.print("DNI: ");
            String dni = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Área: ");
            String area = scanner.nextLine();

            System.out.println("\n--- Datos del Legajo ---");
            System.out.print("Nro Legajo: ");
            String nro = scanner.nextLine();
            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            Legajo legajo = new Legajo(null, false, nro, categoria,
                    EstadoLegajo.ACTIVO, LocalDate.now(), "Creado desde consola");
            Empleado emp = new Empleado(null, false, nombre, apellido, dni, email,
                    LocalDate.now(), area, legajo);

            empleadoService.crear(emp);
            System.out.println(" Empleado y Legajo creados correctamente!");
        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }

    public void listarEmpleados() {
        try {
            System.out.println("\n--- Listado de Empleados ---");
            empleadoService.leerTodos().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(" Error al listar empleados: " + e.getMessage());
        }
    }

    public void actualizarEmpleado() {
        try {
            System.out.print("\nID del empleado a actualizar: ");
            Long id = Long.parseLong(scanner.nextLine());
            Empleado emp = empleadoService.leer(id);
            if (emp == null) {
                System.out.println("No existe empleado con ese ID.");
                return;
            }
            System.out.print("Nuevo área (actual: " + emp.getArea() + "): ");
            String area = scanner.nextLine();
            if (!area.isBlank()) emp.setArea(area);
            empleadoService.actualizar(emp);
            System.out.println(" Empleado actualizado correctamente!");
        } catch (Exception e) {
            System.err.println(" Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarEmpleado() {
        try {
            System.out.print("\nID del empleado a eliminar (baja lógica): ");
            Long id = Long.parseLong(scanner.nextLine());
            empleadoService.eliminar(id);
            System.out.println(" Empleado eliminado lógicamente.");
        } catch (Exception e) {
            System.err.println(" Error al eliminar: " + e.getMessage());
        }
    }

    public void crearLegajo() {
        try {
            System.out.println("\n--- Crear Legajo ---");
            System.out.print("Nro Legajo: ");
            String nro = scanner.nextLine();
            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            Legajo legajo = new Legajo(null, false, nro, categoria,
                    EstadoLegajo.ACTIVO, LocalDate.now(), "Creado manualmente");

            legajoService.crear(legajo);
            System.out.println(" Legajo creado correctamente!");
        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }

    public void listarLegajos() {
        try {
            System.out.println("\n--- Listado de Legajos ---");
            legajoService.leerTodos().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(" Error al listar legajos: " + e.getMessage());
        }
    }
}

