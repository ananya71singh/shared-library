def call(String repoUrl) {
  pipeline {
       agent any
       
       stages {
           
           stage("git") {
               steps {
                   git branch: 'master',
                       url: "${repoUrl}"
               }
           }
           stage('build') {
            steps {
                script {
                    def mvnhome = tool name: 'jenkins-maven', type: 'maven'
                    bat "${mvnhome}/bin/mvn clean install"
                }
            }

        }
        stage('deploy'){
            steps{
            nexusArtifactUploader artifacts: [[artifactId: 'my-app', classifier: '', file: 'C:\\Users\\User\\.jenkins\\jobs\\Case-study-1\\target\\my-app-1.0-SNAPSHOT.jar', type: 'jar']], credentialsId: 'eb52cb8e-67dc-40a3-aba9-96ddb7d2971d', groupId: 'com.mycompany.app', nexusUrl: 'localhost:8081/', nexusVersion: 'nexus3', protocol: 'http', repository: 'http://localhost:8081/repository/SAMPLE-REl/', version: '1.0-SNAPSHOT'}
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
