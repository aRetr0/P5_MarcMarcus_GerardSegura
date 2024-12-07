package backtracking;

import estructura.Encreuades;
import estructura.PosicioInicial;

public class SolucioBacktracking {

    private final Encreuades repte;
    private char[][] solucioActual;
    private char[][] millorSolucio;
    private int millorValor;
    private boolean[] utilitzats;

    public SolucioBacktracking(Encreuades repte) {
        this.repte = repte;
        this.solucioActual = repte.getPuzzle();
        this.millorSolucio = null;
        this.millorValor = 0;
        this.utilitzats = new boolean[repte.getItemsSize()];
    }

    public char[][] getMillorSolucio() {
        return millorSolucio;
    }

    public Runnable start(boolean optim) {
        if (!optim) {
            if (!this.backUnaSolucio(0)) throw new RuntimeException("solució no trobada");
            guardarMillorSolucio();
        } else if (optim) {
            this.backMillorSolucio(0);

        }
        return null;
    }

    private boolean backUnaSolucio(int indexUbicacio) {
        if (indexUbicacio >= repte.getEspaisDisponibles().size()) {
            for (boolean b : utilitzats) {
                if (!b) return false; // Ensure all items are used
            }
            return true;
        }
        for (int indexItem = 0; indexItem < this.repte.getItemsSize(); indexItem++) {
            if (!utilitzats[indexItem] && acceptable(indexUbicacio, indexItem)) {
                anotarASolucio(indexUbicacio, indexItem);
                utilitzats[indexItem] = true; // Mark item as used
                if (backUnaSolucio(indexUbicacio + 1)) {
                    return true;
                }
                desanotarDeSolucio(indexUbicacio, indexItem);
                utilitzats[indexItem] = false; // Unmark item
            }
        }
        return false;
    }


    private void backMillorSolucio(int indexUbicacio) {
        System.out.println("Entering backMillorSolucio with indexUbicacio: " + indexUbicacio);
        if (indexUbicacio >= repte.getEspaisDisponibles().size()) {
            int valorActual = calcularFuncioObjectiu(solucioActual);
            if (valorActual > millorValor) {
                millorValor = valorActual;
                guardarMillorSolucio();
            }
            System.out.println("Exiting backMillorSolucio with indexUbicacio: " + indexUbicacio + " (base case)");
            return;
        }

        PosicioInicial pos = repte.getEspaisDisponibles().get(indexUbicacio);
        int row = pos.getInitRow();
        int col = pos.getInitCol();
        int length = pos.getLength();
        char direccio = pos.getDireccio();

        // Skip cells that are '▪'
        boolean skip = false;
        if (direccio == 'H') {
            for (int i = 0; i < length; i++) {
                System.out.println("Checking cell: (" + row + ", " + (col + i) + ") with value: " + solucioActual[row][col + i]);
                if (solucioActual[row][col + i] == '▪') {
                    skip = true;
                    break;
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                System.out.println("Checking cell: (" + (row + i) + ", " + col + ") with value: " + solucioActual[row + i][col]);
                if (solucioActual[row + i][col] == '▪') {
                    skip = true;
                    break;
                }
            }
        }
        if (skip) {
            System.out.println("Skipping indexUbicacio: " + indexUbicacio + " due to '▪' cell");
            backMillorSolucio(indexUbicacio + 1);
            return;
        }

        for (int indexItem = 0; indexItem < this.repte.getItemsSize(); indexItem++) {
            if (!utilitzats[indexItem] && acceptable(indexUbicacio, indexItem)) {
                anotarASolucio(indexUbicacio, indexItem);
                utilitzats[indexItem] = true; // Mark the item as used
                backMillorSolucio(indexUbicacio + 1);
                desanotarDeSolucio(indexUbicacio, indexItem);
                utilitzats[indexItem] = false; // Unmark the item
            }
        }
        System.out.println("Exiting backMillorSolucio with indexUbicacio: " + indexUbicacio);
    }

    private boolean acceptable(int indexUbicacio, int indexItem) {
        PosicioInicial pos = repte.getEspaisDisponibles().get(indexUbicacio);
        char[] item = repte.getItem(indexItem);
        int row = pos.getInitRow();
        int col = pos.getInitCol();
        int length = pos.getLength();
        char direccio = pos.getDireccio();

        if (item.length > length) return false;

        for (int i = 0; i < item.length; i++) {
            if (direccio == 'H') {
                if (solucioActual[row][col + i] != ' ' && solucioActual[row][col + i] != item[i]) return false;
            } else {
                if (solucioActual[row + i][col] != ' ' && solucioActual[row + i][col] != item[i]) return false;
            }
        }
        return true;
    }

    private void anotarASolucio(int indexUbicacio, int indexItem) {
        PosicioInicial pos = repte.getEspaisDisponibles().get(indexUbicacio);
        char[] item = repte.getItem(indexItem);
        int row = pos.getInitRow();
        int col = pos.getInitCol();
        char direccio = pos.getDireccio();

        for (int i = 0; i < item.length; i++) {
            if (direccio == 'H') {
                solucioActual[row][col + i] = item[i];
            } else {
                solucioActual[row + i][col] = item[i];
            }
        }

        utilitzats[indexItem] = true;
    }

    private void desanotarDeSolucio(int indexUbicacio, int indexItem) {
        PosicioInicial pos = repte.getEspaisDisponibles().get(indexUbicacio);
        char[] item = repte.getItem(indexItem);
        int row = pos.getInitRow();
        int col = pos.getInitCol();
        char direccio = pos.getDireccio();

        for (int i = 0; i < item.length; i++) {
            if (direccio == 'H') {
                if (esborrable(row, col + i, item[i], direccio)) solucioActual[row][col + i] = ' ';
            } else {
                if (esborrable(row + i, col, item[i], direccio)) solucioActual[row + i][col] = ' ';
            }
        }

        utilitzats[indexItem] = false;
    }

    private boolean esborrable(int row, int col, char c, char direccio) {
        boolean creuat = false;

        if (solucioActual[row][col] != c) {
            return false;
        }

        if (direccio == 'H') {
            if (row + 1 < solucioActual.length && (solucioActual[row + 1][col] != ' ' && solucioActual[row + 1][col] != '▪')) creuat = true;
            if (row - 1 >= 0 && (solucioActual[row - 1][col] != ' ' && solucioActual[row - 1][col] != '▪')) creuat = true;
        } else {
            if (col + 1 < solucioActual[0].length && (solucioActual[row][col + 1] != ' ' && solucioActual[row][col + 1] != '▪')) creuat = true;
            if (col - 1 >= 0 && (solucioActual[row][col - 1] != ' ' && solucioActual[row][col - 1] != '▪')) creuat = true;
        }

        return !creuat;
    }

    private boolean esSolucio() {
        return repte.getEspaisDisponibles().isEmpty();
    }

    private int calcularFuncioObjectiu(char[][] matriu) {
        int valor = 0;
        for (char[] fila : matriu) {
            for (char c : fila) {
                if (c != ' ' && c != '▪') {
                    if (Character.isDigit(c)) {
                        valor += c - '0'; // Numeric value
                    } else if (Character.isLetter(c)) {
                        valor += Character.toUpperCase(c) - 'A' + 10; // Offset letters to avoid overlap
                    }
                }
            }
        }
        return valor;
    }

    private void guardarMillorSolucio() {
        millorSolucio = new char[solucioActual.length][];
        for (int i = 0; i < solucioActual.length; i++) {
            millorSolucio[i] = solucioActual[i].clone();
        }
    }

    public String toString() {
        StringBuilder resultat = new StringBuilder();
        for (char[] fila : solucioActual) {
            for (char c : fila) {
                resultat.append(c).append(' ');
            }
            resultat.append('\n');
        }
        return resultat.toString();
    }
}