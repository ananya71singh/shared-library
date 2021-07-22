def call(String repoUrl) {
  pipeline {
       agent any
       
       stages {
           
           stage("Checkout Code") {
               steps {
                   git branch: 'master',
                       url: "${repoUrl}"
               }
           }
           stage('Build') {
            steps {
                script {
                    def mvnhome = tool name: 'jenkins-maven', type: 'maven'
                    bat "${mvnhome}/bin/mvn clean install"
                }
            }

        }
        stage('package publisher'){
            steps{
               // nexusArtifactUploader credentialsId: 'cc14fffd-8917-4df8-b5dc-53a1207b4819', groupId: 'com.mycompany.app', nexusUrl: 'localhost:8081/', nexusVersion: 'nexus3', protocol: 'http', repository: 'maven-snapshots', version: '1.0-SNAPSHOT'
            }
        }
       }
    post {
failure {
  script {
    if (currentBuild.currentResult == 'FAILURE') {
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: "ananya.singh1080ti@gmail.com", sendToIndividuals: true])
    }
  }
}
success{
    script{
    if (currentBuild.currentResult == 'SUCCESS') {
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: "ananya.singh1080ti@gmail.com", sendToIndividuals: true])
    }
    }
}
  
}
   }
}
