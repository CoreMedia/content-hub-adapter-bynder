# Editorial Quick Start

--------------------------------------------------------------------------------

\[[Up](README.md)\] \[[Top](#top)\]

--------------------------------------------------------------------------------

## Table of contents

* [Introduction](#introduction)
* [Browsing the content-hub-adapter-bynder](#browsing-the-content-hub-adapter-bynder)
    * [Basic adapter configuration](#basic-adapter-configuration)
        * [Global adapter configuration](#global-adapter-configuration)
        * [Site-specific adapter configuration](#site-specific-adapter-configuration)
    * [Detailed adapter configuration](#detailed-adapter-configuration)
        * [Basic structure](#basic-structure)
        * [Bynder-specific configuration](#required-configuration)
        * [Multiple connections](#multiple-connections)
        * [Example](#example)     
* [Usage](#usage)    

## Introduction

As mentioned in the main documentation, the Content Hub Adapter for Bynder plugin
provides access to configurable Bynder accounts. Common to all
Content Hub Adapters is the appearance in CoreMedia Studio. The image below shows 
multiple configured Content Hub Adapters in Studio (Content Hub Adapter for Bynder is marked with a blue background).

![Image1: Studio appearance with configured adapters](images/editorial/editorial-documentation_1.png)

## Types of Content Working with This Adapter
- Images
  
## Browsing the Bynder Repository
Depending on the configuration of the adapter, the appearance of the tree may vary. The following sections 
take care of all configuration **places** and **options**.

### Basic Adapter Configuration
This section covers the two possibilities to enable the content-hub-adapter-bynder integration. Please note that those
options are valid for all content-hub-adapters. Before configuring the adapter, please refer to the documentation [Content hub configuration](https://documentation.coremedia.com/cmcc-10/artifacts/2104/webhelp/deployment-en/content/Studio-Contenthub-Configuration.html)
for preliminary steps.

#### Global Adapter Configuration
To enable the content-hub-adapter-bynder for all sites, it is necessary to create a _Settings_ document inside folder:
* _/Settings/Options/Settings/Content Hub/Connections/_
For convenience reasons, naming proposal of this document is "Bynder" (name of the third-party system)

#### Site-Specific Adapter Configuration
To enable the content-hub-adapter-bynder for a single site, it is necessary to create a _Settings_ document inside folder:
* _Options/Settings/Content Hub/Connections/_ (relative to the site's root folder)
For convenience reasons, naming proposal of this document is "Bynder" (name of the third-party system)


### Detailed Adapter Configuration

#### Basic Structure
The table below shows the initial top-level entry for all Content Hub configurations.

| Key         | Type       | Required   |
|-------------|------------|------------|
| connections | StructList | Yes        |

After creation of the initial struct list called **connections** the next step is to create the first entry. This can be done 
in Studio with the struct editor by pressing "Add item to ListProperty". Under this new item some basic settings are added. The table below shows the entries which are common for all connectors. 

| Key           | Type       | Value                 | Required   |
|---------------|------------|------------           |------------|
| connectionId  | String      | <SOME_UNIQUE_ID>     | Yes        |
| factoryId     | String      | bynder    | Yes        |
| enabled       | Boolean     | true or false        | Yes        |
| settings      | Struct     |                      | Yes        |
          

#### Bynder-Specific Configuration
In section [Basic structure](#basic-structure) and according to the table, the settings struct is currently empty.
The settings struct itself holds specific configuration options for the connector (common to all connectors).
The table below depicts all potential entries. 

| Key               | Type       | Value                                                    | Required   | 
|---------------    |------------|------------                                              |------------|
| displayName       | String     | Name of the connection's root folder to display in Studio             | No         |
| apiEndpoint         | String     | URL of Bynder REST API endpoint including API base path _(/api/v4/)_         | Yes         |
| accessToken            | String     | Access Token for Bynder account          | Yes         |

## Multiple Connections

More than one connection can be configured, if required. Click "Add item to ListProperty" again on the _connections_ node in the settings and repeat configuration steps for the new connection.

#### Example
The image below depicts a full configuration of the Content Hub Adapter for Bynder in global space.

![Image2: Full adapter configuration](images/editorial/editorial-documentation_2.png)

## Usage
Once the connector is configured, the "Bynder" named tree appears in the library. By clicking on "Bynder",
the tree expands to all configured Bynder connections. Each connection folder shows the latest assets from the corresponding Bynder repository. The image below shows the appearance.

![Image2: Expanded Studio tree](images/editorial/editorial-documentation_3.png)  

Assets can be imported into the CoreMedia CMS. The picture below shows the 
button for import (purple frame).

![Image2: Expanded Studio tree](images/editorial/editorial-documentation_4.png)  

Keep in mind that certain requirements on the connected Bynder account need to be met to allow download of full-size images to transfer them to the CoreMedia repository. See <https://bynder.docs.apiary.io/#reference/download/download-operations/retrieve-asset-download-location>.

**SEARCH**
