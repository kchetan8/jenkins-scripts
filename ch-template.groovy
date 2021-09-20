def call(Map pipelineParams) {
pipeline {
    agent any
    stages {
        stage('Git Checkout') {
            steps {
                git branch: pipelineParams.branch, credentialsId: 'Githubpersonal8', url: pipelineParams.gitUrl 
            }
        }
        stage('Maven Build') {
            steps {
                bat '''mvn clean install -DskipMunitTests'''
            }

        }
        stage('CH Sandbox Deployment') {
            steps {

            withCredentials([usernamePassword(credentialsId: 'Anypoint14', passwordVariable: 'mule-password', usernameVariable: 'mule-username')]) {
                bat '''mvn package deploy -DmuleDeploy -DskipMunitTests -Dusername=%mule-username% -Dpassword=%mule-password% -Dapp.name=%app-name%-pipelineParams.environment -Dregion=pipelineParams.deploymentRegion -Denvironment=pipelineParams.environment'''
            }

            }
        }

    }
}