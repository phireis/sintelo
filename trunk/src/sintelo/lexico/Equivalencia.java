/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sintelo.lexico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author phireis
 */
public class Equivalencia {

    private List<ClasseEquivalencia> classes;
    private Map<Estado, ClasseEquivalencia> mapa;

    public Equivalencia() {
        classes = new ArrayList<ClasseEquivalencia>();
        mapa = new HashMap<Estado, ClasseEquivalencia>();
    }

    public Equivalencia(Automato automato){
        this();
        Map<Integer, ClasseEquivalencia> mapaClasses = new HashMap<Integer, ClasseEquivalencia>();
        for(Estado e : automato.getEstados()){
            int rec = e.getReconhecedor();
            ClasseEquivalencia classe = mapaClasses.get(rec);
            if(classe == null){
                classe = new ClasseEquivalencia();
                mapaClasses.put(rec, classe);
                classes.add(classe);
            }
            classe.add(e);
            mapa.put(e, classe);
        }
    }

    public List<ClasseEquivalencia> getClasses() {
        return Collections.unmodifiableList(classes);
    }



    public boolean equivalentes(Estado q1, Estado q2){
        

        LinkedHashSet<Character> cs = new LinkedHashSet<Character>();
        cs.addAll(q1.getCondicoes());
        cs.addAll(q2.getCondicoes());
        
        for(Character ch : cs){
            Estado d1 = q1.getDestino(ch);
            Estado d2 = q2.getDestino(ch);
            if(mapa.get(d1) != mapa.get(d2)){
                return false;
            }
        }
        return true;
    }
    
    private void verificaEquivalencia(Equivalencia eq, ClasseEquivalencia classe){
        ArrayList<Estado> estados = new ArrayList<Estado>(classe.getEstados());
        int n = estados.size();
        for(int i = 0; i < n; ++i){
            Estado q1 = estados.get(i);
            if(!eq.mapa.containsKey(q1)){
                ClasseEquivalencia cl = new ClasseEquivalencia();
                cl.add(q1);
                eq.classes.add(cl);
                eq.mapa.put(q1, cl);
                for(int j = i + 1; j < n; ++j){
                    Estado q2 = estados.get(j);
                    if(equivalentes(q1, q2)){
                        cl.add(q2);
                        eq.mapa.put(q2, cl);
                    }
                }
            }
        }        
    }
    
    public Equivalencia classifica(){
        Equivalencia anterior;
        Equivalencia atual = this;

        do{
            anterior = atual;
            atual = new Equivalencia();
            for(ClasseEquivalencia cl : anterior.classes){
                anterior.verificaEquivalencia(atual, cl);
            }
        }while(anterior.classes.size() != atual.classes.size());

        return atual;
    }




}
