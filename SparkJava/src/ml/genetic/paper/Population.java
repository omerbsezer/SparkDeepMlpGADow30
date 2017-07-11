package ml.genetic.paper;

public class Population {
	
	Chromosome[] chromosomes;
	

    // Create a population
    public Population(int populationSize, boolean initialise) {
    	chromosomes = new Chromosome[populationSize];
       
        if (initialise) {
            // Loop and create chromosomes
            for (int i = 0; i < size(); i++) {
            	Chromosome newChromosome = new Chromosome();
                newChromosome.generateChromosome();
                saveChromosome(i, newChromosome);
            }
        }
    }
    
    public void printPopulation(){
    	 for (int i = 0; i < size(); i++) {
         	chromosomes[i].printChromosome();
         }
    }

    /* Getters */
    public  Chromosome getChromosome(int index) {
        return chromosomes[index];
    }
    

    public Chromosome getFittest() {
        Chromosome fittest = chromosomes[0];
        // Loop through chromosomes to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getChromosome(i).getFitness()) {
                fittest = getChromosome(i);
            }
        }
        return fittest;
    }
    
   
    // Get population size
    public int size() {
        return chromosomes.length;
    }

    // Save Chromosome
    public void saveChromosome(int index, Chromosome indiv) {
        chromosomes[index] = indiv;
    }
    
}
