package backtracking;

import estructura.Encreuades;
import estructura.PosicioInicial;

public class SolucioBacktracking {

    /* TODO
     * cal definir els atributs necessaris
     */

    private char[][] millorSolucio;


    private final Encreuades repte;


    public SolucioBacktracking(Encreuades repte) {
        this.repte = repte;
    }

    public char[][] getMillorSolucio() {
        return null; //TODO
    }

    public Runnable start(boolean optim) {
        /* TODO
         * cal inicialitzar els atributs necessaris
         */

        this.millorSolucio = this.repte.getPuzzle();

        if (!optim) {
            if (!this.backUnaSolucio(0)) throw new RuntimeException("solució no trobada");
            guardarMillorSolucio();
        } else this.backMillorSolucio(0);
        return null;
    }

    /* esquema recursiu que troba una solució
     * utilitzem una variable booleana (que retornem)
     * per aturar el recorregut quan hàgim trobat una solució
     */
    private boolean backUnaSolucio(int indexUbicacio) {
        if (indexUbicacio >= this.repte.getEspaisDisponibles().size()) return false;

        boolean trobada = false;
        // iterem sobre els possibles elements
        for (int indexItem = 0; indexItem < this.repte.getItemsSize() && !trobada; indexItem++) {
            //mirem si l'element es pot posar a la ubicació actual
            if (acceptable(indexUbicacio, indexItem)) {
                //posem l'element a la solució actual
                anotarASolucio(indexUbicacio, indexItem);

                if (esSolucio(indexUbicacio)) { // és solució si totes les ubicacions estan plenes
                    return true;
                } else trobada = this.backUnaSolucio(indexUbicacio + 1); //inserim la següent paraula
                if (!trobada)
                    // esborrem la paraula actual, per després posar-la a una altra ubicació
                    desanotarDeSolucio(indexUbicacio, indexItem);
            }
        }
        return trobada;
    }

    /* TODO
     * Esquema recursiu que busca totes les solucions
     * no cal utilitzar una variable booleana per aturar perquè busquem totes les solucions
     * cal guardar una COPIA de la millor solució a una variable
     */
    private void backMillorSolucio(int indexUbicacio) {

    }

    private void checkIndex(int indexUbicacio, int indexItem) {
        if (indexUbicacio < 0 || indexUbicacio >= this.repte.getEspaisDisponibles().size())
            throw new IllegalArgumentException("Invalid indexUbicacio: " + indexUbicacio);

        if (indexItem < 0 || indexItem >= this.repte.getItemsSize())
            throw new IllegalArgumentException("Invalid indexItem: " + indexItem);

    }

    private boolean acceptable(int indexUbicacio, int indexItem) {
        checkIndex(indexUbicacio, indexItem);

        PosicioInicial ubicacio = this.repte.getEspaisDisponibles().get(indexUbicacio);
        char[] paraula = this.repte.getItem(indexItem);
        char[][] puzzle = this.repte.getPuzzle();

        if (ubicacio == null || paraula == null || puzzle == null) {
            throw new NullPointerException("Un dels paràmetres és null");
        }

        // Comprova si la paraula és de la longitud correcta
        if (paraula.length != ubicacio.getLength()) return false;

        // Comprova si la paraula encaixa en la ubicació
        for (int i = 0; i < paraula.length; i++) {
            int row = ubicacio.getInitRow();
            int col = ubicacio.getInitCol();
            switch (ubicacio.getDireccio()) {
                case 'H':
                    col += i;
                    break;
                case 'V':
                    row += i;
                    break;
                default:
                    throw new IllegalArgumentException("Direcció invàlida: " + ubicacio.getDireccio());
            }
            if (row < 0 || row >= puzzle.length || col < 0 || col >= puzzle[0].length) {
                throw new ArrayIndexOutOfBoundsException("Posició fora de la matriu");
            }
            if (puzzle[row][col] != ' ' && puzzle[row][col] != paraula[i]) return false;
        }
        return true;
    }

    private void anotarASolucio(int indexUbicacio, int indexItem) {
        PosicioInicial ubicacio = this.repte.getEspaisDisponibles().get(indexUbicacio);
        char[] paraula = this.repte.getItem(indexItem);
        char[][] puzzle = this.repte.getPuzzle();

        int row = ubicacio.getInitRow();
        int col = ubicacio.getInitCol();

        for (int i = 0; i < paraula.length; i++) {
            if (row < 0 || row >= puzzle.length || col < 0 || col >= puzzle[0].length) {
                throw new ArrayIndexOutOfBoundsException("Posició fora de la matriu");
            }
            puzzle[row][col] = paraula[i];

            int[] newPosition = updatePosicio(row, col, ubicacio.getDireccio());
            row = newPosition[0];
            col = newPosition[1];
        }
    }

    private void desanotarDeSolucio(int indexUbicacio, int indexItem) {
        PosicioInicial ubicacio = this.repte.getEspaisDisponibles().get(indexUbicacio);
        char[] paraula = this.repte.getItem(indexItem);
        char[][] puzzle = this.repte.getPuzzle();

        int row = ubicacio.getInitRow();
        int col = ubicacio.getInitCol();

        for (char c : paraula) {
            if (row < 0 || row >= puzzle.length || col < 0 || col >= puzzle[0].length) {
                throw new ArrayIndexOutOfBoundsException("Posició fora de la matriu");
            }
            if (puzzle[row][col] != c) {
                throw new IllegalArgumentException("La paraula no es troba a la posicio indicada");
            }

            int[] newPosition = updatePosicio(row, col, ubicacio.getDireccio());
            row = newPosition[0];
            col = newPosition[1];
        }

        row = ubicacio.getInitRow();
        col = ubicacio.getInitCol();

        for (int i = 0; i < paraula.length; i++) {

            puzzle[row][col] = ' ';

            int[] newPosition = updatePosicio(row, col, ubicacio.getDireccio());
            row = newPosition[0];
            col = newPosition[1];
        }
    }


    private boolean esSolucio(int index) {
        PosicioInicial ubicacio = this.repte.getEspaisDisponibles().get(index);
        char[][] puzzle = this.repte.getPuzzle();

        int row = ubicacio.getInitRow();
        int col = ubicacio.getInitCol();

        for (int i = 0; i < ubicacio.getLength(); i++) {
            if (puzzle[row][col] == ' ') return false;
            int[] newPosition = updatePosicio(row, col, ubicacio.getDireccio());
            row = newPosition[0];
            col = newPosition[1];
        }
        return true;
    }

    private int calcularFuncioObjectiu(char[][] matriu) {
        int suma = 0;
        for (int i = 0; i < matriu.length; i++) {
            for (int j = 0; j < matriu[i].length; j++) {
                suma += matriu[i][j];
            }
        }
        return suma;
    }

    private void guardarMillorSolucio() {
        // TODO - cal guardar un clone
    }

    private int[] updatePosicio(int row, int col, char direccio) {
        switch (direccio) {
            case 'H':
                col++;
                break;
            case 'V':
                row++;
                break;
            default:
                throw new IllegalArgumentException("Direcció invàlida: " + direccio);
        }
        return new int[]{row, col};
    }

    public String toString() {
        String resultat = "";
        //TODO
        return resultat;
    }
}
