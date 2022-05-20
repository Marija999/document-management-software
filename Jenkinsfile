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
  //post{ 
    //failure{ //kada je build neuspesan 
         //mail to : milicm@comtrade.com , subject = 'The pipeline failed'
    //}
  //}
}
