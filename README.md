# This project is retired and selected parts were incorporated in https://github.com/buckning/myzappi

# Client to interact with the myenergi API

The APIs are documented on the [MyEnergi-App-Api Github page](https://github.com/twonk/MyEnergi-App-Api).

This client requires two bits of information, a serial number of the myenergi hub and an API key.

To get the serial number of the hub, navigate and log in to your myenergi account [here](https://myaccount.myenergi.com/).

Click on [Manage Products](https://myaccount.myenergi.com/location#products). The serial number will be listed under
the device information and will be prefixed with SN.

To generate an API key, click on **Advanced** under the device on the [Manage Products](https://myaccount.myenergi.com/location#products) page.
A pop-up will open and there will be a **Generate new API Key** button. A new pop-up will open with the API key.

# Alexa Skill
There is an Alexa skill under the com.amcglynn.myenergi.aws directory which allows the user to set the charge mode
by saying the following.
* "Alexa, ask My Zappi to start charging". Note that it may take a few minutes for the Zappi to change the charge mode

# Create/Configure the Skill

## Create the Lambda
* Create account/log in to [AWS console](https://aws.amazon.com/console/)
* Search for **Lambda** and click on the link in the search results
* Click **Create Function**
* Choose Author from scratch
* Add name for the function, e.g. myZappi
* Choose Java 11 runtime
* Choose arm64 architecture
* Click on the **Create function** button in the bottom right of the screen

## Configure your API Key and Hub serial number
* Click on the **Configuration** tab and select **Environment variables** in the panel on the left-hand side
* Click on **Edit** to add environment variables and click on the **Add environment variable** button
* Enter **myEnergiHubSerialNumber** as the key and enter the serial number of your hub. This can be found in the first section of this document
* Repeat the process for **myEnergiHubApiKey** and then click **Save**

## Create the Alexa skill
* Log in to [Alexa console](https://developer.amazon.com/alexa/console/ask)
* Click **Create Skill**
* Enter the skill name, e.g. **My Zappi**, and choose the primary locale.
* Choose **Other** as the type of experience
* Select **Custom** model
* Choose **Provision your own** for Hosting services and click Next
* Choose **Start from Scratch** for the Template and click Next
* Click **Create Skill** to complete the wizard

## Configure the skill
1. Click on the **Build** tab and click on the **Interaction Model** dropdown in the pane on the left-hand side
2. Click on **JSON Editor**, copy and paste the content from alexa/InteractionModel.json from this project into the JSON Editor
3. Click on **Save Model**
4. Click on **Endpoint** in the pane on the left-hand side
5. Copy the value for **Your Skill ID**
6. In your AWS console page for your Lambda, which was created above, click on **Add trigger** in the **Function overview** section
7. Select **Alexa** as the source
8. Under Skill ID verification, ensure that **Enable** is selected and then enter the skill ID which was already copied in step 5 and click **Add**
9. Once this is configured, you will be redirected to the Function overview section again. Copy the **Function ARN**
10. Navigate back to the Alexa console and to the Endpoint section. Paste the Function ARN from the previous step into the Default Region text box and click **Save Endpoints**

## Build this project and upload to Lambda
### Upload directly from UI
1. Build this project by running the following Maven command: `mvn clean package`
2. In the AWS console under the Lambda settings in the previous section, click on the **Code** tab, click on the **Upload from** dropdown
3. Click on the **.zip or .jar file** option from the dropdown
4. Click on the **Upload** button on the pop up
5. Navigate to the **target** directory of this project and select **myenergi-client-0.0.1-SNAPSHOT.jar** and click save
6. Click on the **Edit** button under the Lambda **Runtime settings**. Ensure Java 11 is selected and then change the **Handler** to *com.amcglynn.myenergi.aws.MySkillStreamHandler::handleRequest*, then click **Save**

### Upload to S3
1. Log in to S3 - https://s3.console.aws.amazon.com/
1.a. Click **Create bucket**. This step just needs to be done once
1.b. Name the bucket, select the region where your lambda is configured and then use all defaults and click **Create bucket**. This step just needs to be done once
2. Next select the bucket that was just created on the list
3. Click upload, select the jar file from your file system and click **Upload**
4. Click on the file that was just uploaded
5. Copy the S3 URI. This needs to be used in the lambda configuration
6. Navigate back to Lambda, click on the **Code** tab, click on **Amazon S3 location** and enter the S3 URI from the previous step into the text box and click save

## Test the Alexa Skill through the Alexa console
1. Click on the **Test** tab in the Alexa console
2. Select **Development** from the dropdown
3. Under the **Alexa Simulator** type the following command: **Ask my zappi to switch to solar**

# TODO
* Add usage for a requested month / year
* Add optional linear interpolation for when data points are missing
* Unlock charger for immediate charging
* Set and read Zappi Boost times
* Add lock status to get status retrieval
* Integrate Alexa Settings APIs
* Alexa Presentation Language (APL)
* Add to flash briefing
* Get cost of charging the car
