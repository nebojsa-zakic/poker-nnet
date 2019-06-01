/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 *
 * @author user777
 */
public class FuzzyDecider {
    
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String putanja= "fcl/fuzzy_logic.fcl";
		
		FIS fis= FIS.load(putanja);
		
		if(fis==null) {
			System.out.println("nema tog fajla");
			return;
		}
		//crtanja dijagrama pripadanja
		JFuzzyChart.get().chart(fis.getFunctionBlock("decision"));
		fis.setVariable("money", 10050);
                fis.setVariable("certainty", 100);
		fis.setVariable("aggression", 100);
                
		fis.evaluate();
                
                		
		double izlaz= fis.getVariable("decision").getValue();
		System.out.println("Odluka je "+ izlaz);
				

	}

}
