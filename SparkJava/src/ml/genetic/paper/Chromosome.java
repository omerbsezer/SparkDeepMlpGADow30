package ml.genetic.paper;

public class Chromosome {
	
	static int defaultGeneLength = 8;
    private byte[] genes = new byte[defaultGeneLength];
    private int fitness = 0;
  

    //Create a random chromesome
    public void generateChromosome() {
    	byte gene = 0;
        for (int i = 0; i < size(); i++) {
        	if(i==0 || i==4){
        		int randomMax=40;
        		int randomMin=5;
        		gene= (byte)(Math.random()*(randomMax-randomMin)+randomMin);
        	}else if(i==2 || i==6){
        		int randomMax=95;
        		int randomMin=60;
        		gene= (byte)(Math.random()*(randomMax-randomMin)+randomMin);
        	}else if(i==1 || i==3 || i==5 || i==7){
        		int randomMax=20;
        		int randomMin=5;
        		gene= (byte)(Math.random()*(randomMax-randomMin)+randomMin);
        	}
            genes[i] = gene;
            //System.out.print(gene+",");
        }
        //System.out.println("");
    }
    public void printChromosome(){
    	 for (int i = 0; i < size(); i++) {
    		 System.out.print(genes[i]+",");
    	 }
    	 System.out.println("");
    }

    public String stringBuilder(){
    	String line="";
    	line=line+"1 "+"1:"+genes[0]+".0"+" 2:"+genes[1]+".0"+" 3:0.0"+"\n";
    	line=line+"2 "+"1:"+genes[2]+".0"+" 2:"+genes[3]+".0"+" 3:0.0"+"\n";
    	line=line+"1 "+"1:"+genes[4]+".0"+" 2:"+genes[5]+".0"+" 3:1.0"+"\n";
    	line=line+"2 "+"1:"+genes[6]+".0"+" 2:"+genes[7]+".0"+" 3:1.0"+"\n";
    	
    	/*for (int i = 0; i < size()/2; i++) {
    		line=line+genes[i]+";";
    	}
    	line=line+"1"+"\n";
    	for (int i = size()/2; i < size(); i++) {
    		line=line+genes[i]+";";
    	}
    	line=line+"2"+"\n";*/
    	return line;
    }
    

    public static void setDefaultGeneLength(int length) {
    	defaultGeneLength = length;
    }
    
    public byte getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, byte value) {
        genes[index] = value;
        fitness = 0;
    }
    
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}


    public int size() {
        return genes.length;
    }

    public int getFitness() {
        if (fitness == 0.0) {
            fitness = FitnessCalc.getFitnessCalc(this);
            //System.out.println("fitness:"+fitness); 
        }
        return fitness;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i)+" ";
        }
        return geneString;
    }
    
    public String toPrint() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
        	geneString += getGene(i)+" ";
        }
        geneString =geneString+ ":" + fitness;
        return geneString;
    }
}
