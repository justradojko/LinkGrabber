# LinkGrabber

At start, APP fills terms from terms.txt to local stack. Max 3 Threads are grabbing terms from 
this stack and grabbing JSON from the link:

http://www.nature.com/opensearch/request?httpAccept=application/json&query=

where grabbed term is appended at the end. From returned JSON, first link value is grabbed from 
the path   {.."feed": {.. "entry" : { .. "link" : 

This link is then saved to termsAndLinks.txt and to Database using Hibernate Framework.
