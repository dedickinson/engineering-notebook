title:	Submission of METS packages DSpace 4.2 via the SWORD 2 API  
author:	Duncan Dickinson  
licence:	CC-BY-4.0
source:	https://bitbucket.org/duncan_dickinson/dspace-investigation

# Introduction
![CC-BY-4.0 International License](https://i.creativecommons.org/l/by/4.0/80x15.png "CC-BY-4.0 International License")[^cc-by]

[^cc-by]:  [This work is licensed under a  Creative Commons Attribution 4.0 International License.](http://creativecommons.org/licenses/by/4.0/)

> Source <https://bitbucket.org/duncan_dickinson/dspace-investigation>

This document has been prepared to provide an overview regarding the process of submitting a METS package to DSpace 4.2 via a SWORD 2 API. MODS will be used for conveying descriptive metadata.

This work focusses on the use of DSpace as a repository for research data and, as such, the metadata and other structures used reflect this use case. 

## Audience
It is assumed that this document is being read by a person with programming and system administration skills. 

## Prerequisites
This document does not describe how to install DSpace. It is assumed that your DSpace is at Death Star status and is __fully operational__

## Warranty
This document is provided as-is and any queries should be directed to the [DSpace Technical Mailing List](https://lists.sourceforge.net/lists/listinfo/dspace-tech) and not the author(s).

## References

1. DSpace:
	1. [The DSpace SWORD API module](https://wiki.duraspace.org/display/DSDOC4x/SWORDv2+Server)
	2. [The DSpace METS SIP Profile](https://wiki.duraspace.org/display/DSPACE/DSpaceMETSSIPProfile)
	3. [The DSpace code (git) repository](https://github.com/DSpace/DSpace)
3. [SWORD 2.0 Profile](http://swordapp.org/sword-v2/sword-v2-specifications/)
4. [METS](http://www.loc.gov/standards/mets/mets-home.html)
5. [MODS](http://www.loc.gov/standards/mods/)

# Architecture
This section describes the high-level architecture pertaining to the 

The OAIS model[^oais] provides guidance for the repository being implemented:

*	DSpace is treated as a recipient of objects from a Producer system (perhaps a separate submission tool or research tool)
*	All submissions are "trusted" and not mediated by DSpace
	*	i.e. there's no workflow

[^oais]: See <http://en.wikipedia.org/wiki/Open_Archival_Information_System>

# DSpace Configuration

This investigation used the following SWORD v2 Server configuration (in `[dspace]/config/modules/swordv2-server.cfg`)[^confignote]

|Property|Use Default|Value|  
| ------	| :------:	| ------	|  
|`url`	| Y	| na	|  
|`collection.url`	| Y	| na	|  
|`servicedocument.url`	| Y	| na	|  
|`accepts`	| Y	| `*/*`	|  
|`keep-original-package`	| Y	| `true`	|  
|`bundle.name`	| Y	| `SWORD`	|  
|`keep-package-on-fail`	| N	| `true`	|  
|`failed-package.dir`	| Y	| `${dspace.dir}/upload`	|  
|`on-behalf-of.enable`	| N	| `false`	|  
|`statement.bundles`	| N	| `ORIGINAL, SWORD, LICENSE, METADATA`|  
|`versions.keep`	| Y	| `true`	| 

[^confignote]: For the sake of brevity, not all possible configuration items are listed here. Assume that those not listed appear as per the default distribution file.

# Sample Data

Sample data is provided in the `sample-data` folder.

# Sword 2 client

A command-line client has been provided and 

The source code for this client is located in the `sword2-client` directory

# Submission process


# DSpace Codebase



