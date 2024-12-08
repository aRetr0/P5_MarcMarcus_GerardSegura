package greedy;

import estructura.Encreuades;
import estructura.PosicioInicial;

public class SolucióVoraç {
    private final Encreuades repte;
    private char[][] solucioActual;
    private boolean[] utilitzats;

    public SolucióVoraç(Encreuades repte) {
        this.repte = repte;
        this.solucioActual = repte.getPuzzle();
        this.utilitzats = new boolean[repte.getItemsSize()];
        this.greedy();
    }

    public char[][] getSolucio() {
        return solucioActual;
    }

    private void greedy() {
        for (PosicioInicial pos : repte.getEspaisDisponibles()) {
            for (int indexItem = 0; indexItem < repte.getItemsSize(); indexItem++) {
                if (!utilitzats[indexItem] && acceptable(pos, repte.getItem(indexItem))) {
                    anotarASolucio(pos, repte.getItem(indexItem));
                    utilitzats[indexItem] = true;
                    break;
                }
            }
        }
    }

    private boolean acceptable(PosicioInicial pos, char[] item) {
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

    private void anotarASolucio(PosicioInicial pos, char[] item) {
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
    }
}
