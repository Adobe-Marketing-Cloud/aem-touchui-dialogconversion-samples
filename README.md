# Adobe Experience Manager 6: Dialog Conversion Tool examples

This repository contains sample dialog rewrite rules showing how to customize the AEM Dialog Conversion Tool. The dialog conversion tool can be downloaded on [Package Share](https://www.adobeaemcloud.com/content/marketplace/marketplaceProxy.html?packagePath=/content/companies/public/adobe/packages/cq600/tools/cq-dialog-conversion-content). Visit the [documentation](http://docs.adobe.com/docs/en/aem/6-0/develop/dev-tools/dialog-conversion.html) for more information and instructions.

The repository contains a Java-based rewrite rule (located in the bundle below `/bundle`) and a node-based rewrite rule (located in the content package below `/content`). They are functionally equivalent and show how custom rewrite rules can be implemented. To install the sample rules:

* Make sure the dialog conversion tool is installed on your AEM instance
* Build the modules:
  ```
  $ cd ./my/code/aem-touchui-dialogconversion-samples
  $ mvn clean install
  ```
* Install the content package (it embeds the bundle):
  ```
  $ cd ./content
  $ mvn -P autoInstallPackage clean install
  ```

After installing the sample rules, they should be taken into account by the dialog conversion tool. Note that providing a custom rule at `/apps/cq/dialogconversion/rules` overrides the default rules located in `/libs/cq/dialogconversion/rules`.