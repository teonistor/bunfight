# Bunfight
Every year new students join the university and search for clubs and societies to join to make their uni life one of the best. To facilitate this, a Bunfight event is organised, where each society gets a stall and attempts to attract attention, by being the brightest, loudest, most colorful, most obscene... you name it. In this two-way apocalyptical swarm, it is our responsibility as club committee to stand out and make sure potentially interested people find us.

## The idea
In my first two years of uni and bunfighting, I have come across shittons of ideas, one worse than the other. Typing my name and email into a spreadsheet where I could see all the previous subscribers (and made myself visible to all future ones) was just marginally better than writing such details with pen on paper (how outrageously old-fashioned!). Walls of photos take just one blow of the wind to become a tornado of useless paper, to the despair of surrounding people and trees.

What if, instead, we could show our newcomers a simple sign-up form in a corner of a screen, and let them see a slide show of pictures on the rest of it? That is what I set off to do.

I then realised there remains some unused screen real-estate. And since I have the hobby of collecting jokes, puns, and other fun quotes people say, I decided to create a *Hillwalkers' out-of-context quotes corner* as well.

### SUHC joining the bunfight
This app has been tailored to SUHC in terms of the text and artwork, and the full archive contains photos and quotes from this particular club; but it will only be a small piece of our stall.

And after all, it can easily be changed to suit any other society.

## Technicalities
You will need Java SE 8 (or later) runtime environment installed on your system, which if you don't have you may get by asking uncle Google or from [here unless the link breaks in the future](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).

Then you can execute [Bunfight.jar](https://github.com/teonistor/bunfight/raw/master/Bunfight.jar). But for it to actually work, you must provide next to it:
- A text file *Quotes.txt* containing blocks of text to be displayed on the right side of the app window. These blocks should be separated by one empty line and the file is expected to have an empty line at the end (Unix convention *-ish*).
- A folder *Photos* containing photos for the slide show. If you include non-image files in this folder strange things may happen, particularly if you include *only* non-image files; so don't do it! Or do it, but don't then call me at 2am to complain that your PC is on fire.

Or you can [download the zip file](https://github.com/teonistor/bunfight/raw/master/Bunfight.zip) with dependencies included; extract it and run the JAR.

The app is intended to run in full-screen only. You can Alt+Tab out of it and come back, but when you press Esc it will close. This is in fact how you should close it, unless you particularly enjoy the fingers gymnastics of Alt+F4.

Submissions of the registration form will be recorded in a file members.csv next to the executable JAR. This file will be created if missing and appended to if already in existence.

Of course, if you can't sleep at night because you believe the requirements above are outrageous, no one will stop you from downloading the source code and changing them; after all, this is Github. 
