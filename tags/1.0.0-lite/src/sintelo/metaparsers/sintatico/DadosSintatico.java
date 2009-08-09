
package sintelo.metaparsers.sintatico;


import sintelo.reconhecedor.sintatico.EspecificacaoSintatica;


public class DadosSintatico implements EspecificacaoSintatica {


    private static final int[][] TABELA_PARSE = {
        {1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {3, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, 5, 5, 5, 5, -1, 5, -1, -1, 5, -1, -1, -1, 5, -1, -1},
        {-1, 6, 6, 6, 6, -1, 6, -1, -1, 6, -1, -1, -1, 6, -1, -1},
        {-1, 10, 10, 10, 10, -1, 9, -1, -1, 10, -1, -1, -1, 10, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, 8, -1, -1, 8, -1, -1, -1, 8, 7},
        {-1, 13, 13, 13, 15, -1, -1, -1, -1, 13, -1, -1, -1, 14, -1, -1},
        {-1, 11, 11, 11, 11, -1, -1, 12, -1, 11, 12, -1, -1, 11, 12, 12},
        {-1, 17, 18, 16, -1, -1, -1, -1, -1, 19, -1, -1, -1, -1, -1, -1},
        {-1, 24, 24, 24, 24, -1, -1, 24, -1, 24, 24, 22, 23, 24, 24, 24},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1}
    };


    private static final int[][] PRODUCOES = {
        {17, 18},
        {},
        {17, 18},
        {},
        {2, 29, 5, 19, 7, 31},
        {20},
        {21, 22},
        {15, 30, 21, 22},
        {},
        {6},
        {23, 24},
        {23, 24},
        {},
        {25, 26},
        {27},
        {4, 34},
        {3, 32},
        {1, 32},
        {2, 33},
        {28},
        {9, 35, 20, 10, 36},
        {13, 35, 20, 14, 37},
        {11, 38},
        {12, 39},
        {}
    };


    public int getDerivacao(int s, int t) {
        return TABELA_PARSE[s][t];
    }


    public int[] getProducao(int n) {
        return PRODUCOES[n];
    }


    public int getPrimeiroNaoTerminal() {
        return 16;
    }


    public int getPrimeiraAcao() {
        return 29;
    }


    public int getSimboloInicial() {
        return 16;
    }
}
