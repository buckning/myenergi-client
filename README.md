# Client to interact with the myenergi API

The APIs are documented on the [MyEnergi-App-Api Github page](https://github.com/twonk/MyEnergi-App-Api).

This client requires two bits of information, a serial number of the myenergi hub and an API key.

To get the serial number of the hub, navigate and log in to your myenergi account [here](https://myaccount.myenergi.com/).

Click on [Manage Products](https://myaccount.myenergi.com/location#products). The serial number will be listed under
the device information and will be prefixed with SN.

To generate an API key, click on **Advanced** under the device on the [Manage Products](https://myaccount.myenergi.com/location#products) page.
A pop-up will open and there will be a **Generate new API Key** button. A new pop-up will open with the API key.

# Road map
* Get minute by minute Zappi historical data
* Add optional linear interpolation for when data points are missing
* Get hour by hour Zappi historical data
* Unlock charger for immediate charging
* Set and read Zappi Boost times
* Add lock status to get status retrieval
