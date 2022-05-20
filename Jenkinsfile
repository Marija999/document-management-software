//CODE_CHANGES= getGitChanges()
pipeline{
  agent any
  //environment{sve komande koje napisemo ovde mogu da se koriste kroz ceo kod
  //}
  stages{
    stage("clone"){
      steps{
        //git clone 'https://github.com/Marija999/document-management-software.git'
        echo 'Clone repo'
      }
      
    }
    stage("build"){
      steps{
        echo 'Building the app-with webhook'
      }
    }
    stage("test"){
      //when{
        //expression{
          //BRANCH_NAME=="master" && CODE_CHANGES==true
        //}
      //}
      steps{
        echo 'Testing the app'
      }
    }
    stage("deploy"){
      steps{
        echo 'Deploying the app'
      }
    }
  }
  post{ 
    failure{ 
      mail bcc: '', body: 'I am sorry, try your best again!', cc: 'stasar@comtrade.com anadjj2comtrade.com', from: '', replyTo: '', subject: 'Build Failed', to: 'milicm@comtrade.com'   
    }
    success
    {
      mail bcc: '', body: 'Bravo!', cc: 'stasar@comtrade.com anadjj2comtrade.com', from: '', replyTo: '', subject: 'Build Successful', to: 'milicm@comtrade.com'
    }
  }
}
