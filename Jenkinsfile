CODE_CHANGES= getGitChanges()
pipeline{
  agent any
  //environment{sve komande koje napisemo ovde mogu da se koriste kroz ceo kod
  //}
  stages{
    stage("clone"){
      steps{
        git clone 'https://github.com/logicaldoc/document-management-software.git logicaldoc-communit'
      }
    
      
    }
    stage("build"){
    }
    stage("test"){
      when{
        expression{
          BRANCH_NAME=="master" && CODE_CHANGES==true
        }
    }
      steps{
      }
    }
    stage("deploy"){
    }
  }
  post{ //post mora stajati nakon svih stage-eva
       //always{//nesto sto se uvek izvrsava bez obzira da li je build prosao ili nije ili se promenio
      // }
       //success{//ovo se izvrsava kada je build uspesan
      // }
       failure{//kada je build neuspesan 
         mail to : milicm@comtrade.com , subject = 'The pipeline failed'
       }
  }
}
