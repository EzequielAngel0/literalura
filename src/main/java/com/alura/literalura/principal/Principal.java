package com.alura.literalura.principal;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.alura.literalura.service.BookService;

import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private BookService bookService;

    public Principal(BookService bookService) {
        this.bookService = bookService;
    }

    public void mostrarMenu() {
        int opcion = -1;
        String menu = """

                ╔════════════════════════════════════════╗
                ║         LITERALURA - CATALOGO          ║
                ╠════════════════════════════════════════╣
                ║ 1 - Buscar libro por titulo           ║
                ║ 2 - Listar libros registrados          ║
                ║ 3 - Listar autores registrados         ║
                ║ 4 - Listar autores vivos en un anio   ║
                ║ 5 - Listar libros por idioma           ║
                ║ 6 - Estadisticas de libros             ║
                ║ 7 - Top 10 libros mas descargados      ║
                ║ 8 - Buscar autor por nombre            ║
                ║ 9 - Autores por rango de anios         ║
                ║ 0 - Salir                              ║
                ╚════════════════════════════════════════╝
                Elija una opcion: """;

        while (opcion != 0) {
            System.out.print(menu);
            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> mostrarEstadisticas();
                    case 7 -> mostrarTop10Libros();
                    case 8 -> buscarAutorPorNombre();
                    case 9 -> listarAutoresPorRangoAnios();
                    case 0 -> System.out.println("\n¡Hasta luego!\n");
                    default -> System.out.println("\nOpcion invalida\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nPor favor ingrese un numero valido\n");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.print("\nIngrese el titulo del libro a buscar: ");
        String titulo = scanner.nextLine();

        try {
            System.out.println("\nBuscando en la API de Gutendex...");
            Book libro = bookService.searchAndSaveBook(titulo);

            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║              LIBRO ENCONTRADO Y GUARDADO                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║ Titulo: " + libro.getTitle());

            if (libro.getAuthor() != null) {
                System.out.println("║ Autor: " + libro.getAuthor().getName());

                String periodo = "";
                if (libro.getAuthor().getBirthYear() != null && libro.getAuthor().getDeathYear() != null) {
                    periodo = "(" + libro.getAuthor().getBirthYear() + " - " + libro.getAuthor().getDeathYear() + ")";
                } else if (libro.getAuthor().getBirthYear() != null) {
                    periodo = "(" + libro.getAuthor().getBirthYear() + " - Presente)";
                }

                if (!periodo.isEmpty()) {
                    System.out.println("║    " + periodo);
                }
            }

            System.out.println("║ Idioma: " + libro.getLanguage());
            System.out.println("║ Descargas: " + String.format("%.0f", libro.getDownloads()));
            System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        } catch (Exception e) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                    ERROR                                        ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║ " + e.getMessage());
            System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        }
    }

    private void listarLibrosRegistrados() {
        List<Book> libros = bookService.getAllBooks();
        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              LIBROS REGISTRADOS EN LA BD                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        int contador = 1;
        for (Book libro : libros) {
            System.out.println("║ " + contador + ". " + libro.getTitle());
            System.out.println(
                    "║    Autor: " + (libro.getAuthor() != null ? libro.getAuthor().getName() : "Desconocido"));
            System.out.println("║    Idioma: " + libro.getLanguage());
            System.out.println("║    Descargas: " + String.format("%.0f", libro.getDownloads()));
            if (contador < libros.size()) {
                System.out.println("║    ────────────────────────────────────────────────────────────");
            }
            contador++;
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("Total: " + libros.size() + " libro(s)\n");
    }

    private void listarAutoresRegistrados() {
        List<Author> autores = bookService.getAllAuthors();
        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados en la base de datos\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              AUTORES REGISTRADOS EN LA BD                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        int contador = 1;
        for (Author autor : autores) {
            System.out.println("║ " + contador + ". " + autor.getName());

            String periodo = "";
            if (autor.getBirthYear() != null && autor.getDeathYear() != null) {
                periodo = "(" + autor.getBirthYear() + " - " + autor.getDeathYear() + ")";
            } else if (autor.getBirthYear() != null) {
                periodo = "(" + autor.getBirthYear() + " - Presente)";
            } else {
                periodo = "(Fechas desconocidas)";
            }

            System.out.println("║    " + periodo);

            if (contador < autores.size()) {
                System.out.println("║    ────────────────────────────────────────────────────────────");
            }
            contador++;
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("Total: " + autores.size() + " autor(es)\n");
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("\nIngrese el anio: ");
        try {
            int anio = Integer.parseInt(scanner.nextLine());
            List<Author> autores = bookService.getAuthorsAliveInYear(anio);

            if (autores.isEmpty()) {
                System.out.println("\nNo se encontraron autores vivos en el anio " + anio + "\n");
                return;
            }

            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║         AUTORES VIVOS EN EL ANIO " + anio + "                      ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");

            int contador = 1;
            for (Author autor : autores) {
                System.out.println("║ " + contador + ". " + autor.getName());

                String periodo = "";
                if (autor.getBirthYear() != null && autor.getDeathYear() != null) {
                    periodo = "(" + autor.getBirthYear() + " - " + autor.getDeathYear() + ")";
                } else if (autor.getBirthYear() != null) {
                    periodo = "(" + autor.getBirthYear() + " - Presente)";
                }

                if (!periodo.isEmpty()) {
                    System.out.println("║    " + periodo);
                }

                if (contador < autores.size()) {
                    System.out.println("║    ────────────────────────────────────────────────────────────");
                }
                contador++;
            }

            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            System.out.println("Total: " + autores.size() + " autor(es)\n");
        } catch (NumberFormatException e) {
            System.out.println("\nPor favor ingrese un anio valido\n");
        }
    }

    private void listarLibrosPorIdioma() {
        String menuIdiomas = """

                Seleccione el idioma:
                1 - Espanol (es)
                2 - Ingles (en)
                3 - Frances (fr)
                4 - Portugues (pt)
                Opcion: """;

        System.out.print(menuIdiomas);
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            String idioma = switch (opcion) {
                case 1 -> "es";
                case 2 -> "en";
                case 3 -> "fr";
                case 4 -> "pt";
                default -> {
                    System.out.println("\nOpcion invalida\n");
                    yield null;
                }
            };

            if (idioma != null) {
                List<Book> libros = bookService.getBooksByLanguage(idioma);
                String nombreIdioma = getNombreIdioma(idioma);

                if (libros.isEmpty()) {
                    System.out.println("\nNo se encontraron libros en " + nombreIdioma + "\n");
                    return;
                }

                System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
                System.out.println(
                        "║              LIBROS EN " + nombreIdioma.toUpperCase() + "                              ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");

                int contador = 1;
                for (Book libro : libros) {
                    System.out.println("║ " + contador + ". " + libro.getTitle());
                    System.out.println(
                            "║    " + (libro.getAuthor() != null ? libro.getAuthor().getName() : "Autor desconocido"));

                    if (contador < libros.size()) {
                        System.out.println("║    ────────────────────────────────────────────────────────────");
                    }
                    contador++;
                }

                System.out.println("╚════════════════════════════════════════════════════════════════╝");
                System.out.println("Total: " + libros.size() + " libro(s)\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPor favor ingrese un numero valido\n");
        }
    }

    private String getNombreIdioma(String codigo) {
        return switch (codigo) {
            case "es" -> "Espanol";
            case "en" -> "Ingles";
            case "fr" -> "Frances";
            case "pt" -> "Portugues";
            default -> codigo;
        };
    }

    // ========== ADVANCED FEATURES ==========

    private void mostrarEstadisticas() {
        java.util.DoubleSummaryStatistics stats = bookService.getBookStatistics();

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              ESTADISTICAS DE LIBROS                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Total de libros: " + stats.getCount());
        System.out.println("║ Total de descargas: " + String.format("%.0f", stats.getSum()));
        System.out.println("║ Promedio de descargas: " + String.format("%.2f", stats.getAverage()));
        System.out.println("║ Maximo de descargas: " + String.format("%.0f", stats.getMax()));
        System.out.println("║ Minimo de descargas: " + String.format("%.0f", stats.getMin()));
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    private void mostrarTop10Libros() {
        List<Book> top10 = bookService.getTop10MostDownloaded();

        if (top10.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              TOP 10 LIBROS MAS DESCARGADOS                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        int posicion = 1;
        for (Book libro : top10) {
            System.out.println("║ " + posicion + ". " + libro.getTitle());
            System.out.println("║    " + (libro.getAuthor() != null ? libro.getAuthor().getName() : "Desconocido"));
            System.out.println("║    " + String.format("%.0f", libro.getDownloads()) + " descargas");

            if (posicion < top10.size()) {
                System.out.println("║    ────────────────────────────────────────────────────────────");
            }
            posicion++;
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    private void buscarAutorPorNombre() {
        System.out.print("\nIngrese el nombre del autor a buscar: ");
        String nombre = scanner.nextLine();

        List<Author> autores = bookService.searchAuthorsByName(nombre);

        if (autores.isEmpty()) {
            System.out.println("\nNo se encontraron autores con ese nombre\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              RESULTADOS DE BUSQUEDA                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        int contador = 1;
        for (Author autor : autores) {
            System.out.println("║ " + contador + ". " + autor.getName());

            String periodo = "";
            if (autor.getBirthYear() != null && autor.getDeathYear() != null) {
                periodo = "(" + autor.getBirthYear() + " - " + autor.getDeathYear() + ")";
            } else if (autor.getBirthYear() != null) {
                periodo = "(" + autor.getBirthYear() + " - Presente)";
            }

            if (!periodo.isEmpty()) {
                System.out.println("║    " + periodo);
            }

            if (contador < autores.size()) {
                System.out.println("║    ────────────────────────────────────────────────────────────");
            }
            contador++;
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("Total: " + autores.size() + " autor(es) encontrado(s)\n");
    }

    private void listarAutoresPorRangoAnios() {
        System.out.print("\nIngrese el anio inicial: ");
        try {
            int anioInicial = Integer.parseInt(scanner.nextLine());
            System.out.print("Ingrese el anio final: ");
            int anioFinal = Integer.parseInt(scanner.nextLine());

            if (anioInicial > anioFinal) {
                System.out.println("\nEl anio inicial debe ser menor o igual al anio final\n");
                return;
            }

            List<Author> autores = bookService.getAuthorsByBirthYearRange(anioInicial, anioFinal);

            if (autores.isEmpty()) {
                System.out.println("\nNo se encontraron autores en ese rango de anios\n");
                return;
            }

            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println(
                    "║         AUTORES NACIDOS ENTRE " + anioInicial + " Y " + anioFinal + "                 ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");

            int contador = 1;
            for (Author autor : autores) {
                System.out.println("║ " + contador + ". " + autor.getName());

                String periodo = "";
                if (autor.getBirthYear() != null && autor.getDeathYear() != null) {
                    periodo = "(" + autor.getBirthYear() + " - " + autor.getDeathYear() + ")";
                } else if (autor.getBirthYear() != null) {
                    periodo = "(" + autor.getBirthYear() + " - Presente)";
                }

                if (!periodo.isEmpty()) {
                    System.out.println("║    " + periodo);
                }

                if (contador < autores.size()) {
                    System.out.println("║    ────────────────────────────────────────────────────────────");
                }
                contador++;
            }

            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            System.out.println("Total: " + autores.size() + " autor(es)\n");
        } catch (NumberFormatException e) {
            System.out.println("\nPor favor ingrese anios validos\n");
        }
    }
}
