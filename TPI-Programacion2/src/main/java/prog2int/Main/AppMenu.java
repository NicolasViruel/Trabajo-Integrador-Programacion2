package prog2int.Main;

import java.util.Scanner;

public class AppMenu {
    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.menuHandler = new MenuHandler(scanner);
        this.running = true;
    }

    public void run() {
        while (running) {
            MenuDisplay.mostrarMenuPrincipal();
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1" -> menuHandler.crearEmpleado();
                case "2" -> menuHandler.listarEmpleados();
                case "3" -> menuHandler.actualizarEmpleado();
                case "4" -> menuHandler.eliminarEmpleado();
                case "5" -> menuHandler.crearLegajo();
                case "6" -> menuHandler.listarLegajos();
                case "0" -> {
                    System.out.println("Saliendo del sistema...");
                    running = false;
                }
                default -> System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }
}
