pipeline {
  agent any
  stages {
    stage('Clone source') {
      steps {
        checkout([$class: 'GitSCM',
          branches: [[name: '*/master']],
          extensions: [
            [$class: 'RelativeTargetDirectory', relativeTargetDir: 'cds']
          ],
          userRemoteConfigs: [[url: 'https://github.com/stevshil/java.git']]
        ])
      }
    }
    stage('Build') {
      steps {
        dir('cds/CompDiscDaoWithRestAndBoot') {
          sh 'mvn -Dmaven.test.skip=true clean package'
        }
      }
    }
    stage('Test') {
      steps {
        dir('cds/CompDiscDaoWithRestAndBoot') {
          sh 'mvn test'
        }
      }
    }
    stage('Publish') {
      steps {
        echo 'Publish to our software repository'
      }
    }
  }
}
