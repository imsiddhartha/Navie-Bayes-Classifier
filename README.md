Implemented the Naïve Bayes (NB) classifier for two different datasets from the UCI Machine learning repository (http://archive.ics.uci.edu/ml/),namely the Bank Marketing Dataset (http://archive.ics.uci.edu/ml/datasets/Bank+Marketing) and Breast Cancer Wisconsin Dataset (http://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Diagnostic%29)

DATASET:
	In the first dataset (Bank Marketing Dataset), work with only Discrete/Categorical Data and I have not considered the continuous 		valued features.
	So, I made a subset of the Bank Marketing dataset consisting of discrete valued features only as BankMarketing_Discrete dataset and 	work with this.
	
	For the second dataset (Cancer Dataset), all the features (attributes) are continuous. So,I have to use Gaussian Naïve Bayes (GNB) 		approach wherein you need to estimate Mean and Variance of each attribute and use Gaussian Model for estimating the Likelihoods.

RESULTS:
	Accuracy​ for Breast Cancer Wisconsin (Diagnostic) Data Set: 67.74
	Accuracy​ for Bank Marketing Data Set :				​88.98
