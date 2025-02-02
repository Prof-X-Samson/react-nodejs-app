//Build the Docker Image Using the Dockerfile in the root folder

def buildImage() {
    echo "Build Docker Image with Dockerfile..."
    sh 'docker build -t profxsamson/demo-app:react-nodejs-app .'
}

//Push Docker Image to DockerHub Repository

def pushImage() {
    echo "Pushing Docker Image to Docker Hub Repo..."
    withCredentials([usernamePassword(credentialsId: 'Docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push profxsamson/demo-app:react-nodejs-app'
    }
} 

//Pull Docker Image from DockerHub and Deploy Image to Amazon EC2 Server

def deployImage() {
    echo "Deploying the application to EC2..."
    def dockerCmd = 'docker run -d -p 80:80 --name react-nodejs-app profxsamson/demo-app:react-nodejs-app' 
    sshagent(['awsec2key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@172-31-10-4 ${dockerCmd}"
    }
} 
return this
