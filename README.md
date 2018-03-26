# SparkDeepMlpGADow30
A Deep Neural-Network based Stock Trading System based on Evolutionary Optimized Technical Analysis Parameters

In this study, we propose a stock trading system based on optimized technical analysis parameters for creating buy-sell points
using 	__genetic algorithms__. The model is developed utilizing Apache Spark big data platform. The optimized parameters are then passed to 	__a deep MLP neural network__ for buy-sell-hold predictions. Dow 30 stocks are chosen for model validation. Each Dow stock is trained separately using daily close prices between 1996-2016 and tested between 2007-2016. The results indicate that optimizing the technical indicator parameters not only enhances the stock trading performance but also provides a model that might be used as an alternative to Buy and Hold and other standard technical analysis models. The phase of proposed method is illustrated in below.


![ga_](https://user-images.githubusercontent.com/10358317/37893495-dce7ab0a-30e3-11e8-9c02-fe49ac5ba112.png)


Utilizing optimized technical analysis feature parameter values as input features for neural network stock trading
system is the basis for our proposed model. We used 	__genetic algorithms to optimize RSI parameters__ for uptrend and
downtrend market conditions. Then, we used those optimized feature values as buy-sell trigger points for our deep
neural network data set. We used Dow 30 stocks to validate our model. The results indicate that such a trading
system produces comparable or better results when compared with Buy & Hold and other trading systems for a wide
range of stocks even for relatively longer periods. The structure of the chromosomes and genes in the chromosomes are shown below. 

- RSI Buy values are created randomly between 5 and 40. 
- RSI Buy intervals are created randomly between 5 and 20 days. 
- RSI Sell values are created randomly between 60 and 95. 
- RSI Sell intervals are created randomly between 5 and 20 days. 
- The same procedure is followed to create 4 genes for uptrend.


![chrom](https://user-images.githubusercontent.com/10358317/37893611-414eeedc-30e4-11e8-9315-a9ca9affdbb3.png)







Science Direct Link: http://www.sciencedirect.com/science/article/pii/S1877050917318252

_**Cite as:**_

**Bibtex:**

```
@article{sezer2017deep,
  title={A Deep Neural-Network Based Stock Trading System Based on Evolutionary Optimized Technical Analysis Parameters},
  author={Sezer, Omer Berat and Ozbayoglu, Murat and Dogdu, Erdogan},
  journal={Procedia Computer Science},
  volume={114},
  pages={473--480},
  year={2017},
  publisher={Elsevier}
}
```

**MLA:**

Sezer, Omer Berat, Murat Ozbayoglu, and Erdogan Dogdu. "A Deep Neural-Network Based Stock Trading System Based on Evolutionary Optimized Technical Analysis Parameters." Procedia Computer Science 114 (2017): 473-480

What is Multi Layer Perceptron (MLP)? (General Information): https://en.wikipedia.org/wiki/Multilayer_perceptron

What is Genetic Algorithm?: https://en.wikipedia.org/wiki/Genetic_algorithm

What is Relative Strength Index?: https://en.wikipedia.org/wiki/Relative_strength_index

Apache Spark MLlib: https://spark.apache.org/mllib/
