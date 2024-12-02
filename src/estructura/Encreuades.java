package estructura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Encreuades {

    private final char[][] puzzle;
    private final char[][] items;
    private List<PosicioInicial> espaisDisponibles = new ArrayList<>();

    public Encreuades(char[][] puzzle, char[][] items) {
        this.puzzle = puzzle;
        this.items = items;
        int lengthMinim = trobaLongitudMinima();
        this.espaisDisponibles = trobaEspaisDisponibles(lengthMinim);
        Collections.sort(this.espaisDisponibles, Collections.reverseOrder());
    }

    public char[][] getPuzzle() {
        char[][] other = new char[this.puzzle.length][];
        for (int row = 0; row < this.puzzle.length; row++) {
            other[row] = new char[this.puzzle[row].length];
            System.arraycopy(this.puzzle[row], 0, other[row], 0, this.puzzle[row].length);
        }
        return other;
    }

    public int getItemsSize() {
        return this.items.length;
    }

    public char[] getItem(int index) {
        return this.items[index];
    }

    public List<PosicioInicial> getEspaisDisponibles() {
        return this.espaisDisponibles;
    }

    private int trobaLongitudMinima() {
        int minim = Integer.MAX_VALUE;
        for (char[] row : this.items) {
            if (minim > row.length)
                minim = row.length;
        }
        return minim;
    }

    private List<PosicioInicial> trobaEspaisDisponibles(int lengthMinim) {
        List<PosicioInicial> result = new ArrayList<>();
        int rows = this.puzzle.length;
        int cols = this.puzzle[0].length;

        // Buscar seqüències horitzontals
        for (int i = 0; i < rows; i++) {
            int j = 0;
            while (j <= cols - lengthMinim) {
                if (puzzle[i][j] == ' ') {
                    int start = j;
                    while (j < cols && puzzle[i][j] == ' ') {
                        j++;
                    }
                    int length = j - start;
                    if (length >= lengthMinim) {
                        result.add(new PosicioInicial(i, start, length, 'H'));
                    }
                }
                j++;
            }
        }

        // Buscar seqüències verticals
        for (int j = 0; j < cols; j++) {
            int i = 0;
            while (i <= rows - lengthMinim) {
                if (puzzle[i][j] == ' ') {
                    int start = i;
                    while (i < rows && puzzle[i][j] == ' ') {
                        i++;
                    }
                    int length = i - start;
                    if (length >= lengthMinim) {
                        result.add(new PosicioInicial(start, j, length, 'V'));
                    }
                }
                i++;
            }
        }

        return result;
    }

}
