# Exchange

### For debugging
For debugging you can use Docker-Compose, this will automatically set up a local DynamoDB server for you.
This also means you're not required to set up an AWS client just for developing.
If you do want to test with DynamoDb, you can just use the default Dockerfile.

### Using Twitter
If you want to use Twitter instead of the default console notifier you need to set up the following environment variables:
- `twitter4j.oauth.consumerKey`
- `twitter4j.oauth.consumerSecret`
- `twitter4j.oauth.accessToken`
- `twitter4j.oauth.accessTokenSecret`

You should also set `NOTIFICATION_SERVICE` to "TWITTER" without quotes

For the keys and tokens you need to set up a twitter developer account, and set the app permissions to at least **"Read and write"**.
The `consumerKey` and `consumerSecret` are api key and secret.
For `accessToken` and `accessTokenSecret` you need to generate the access token & secret.

### Setting up DynamoDb
For DynamoDB you'll have to set up an IAM user in AWS. At the end of the creation process you'll need to save accessKey and secretKey. 
Set the following environment variables with those values:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`


### Docker container
I've already created a Docker container ready to use, which can be found here:  
https://hub.docker.com/r/subside/exchange