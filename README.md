
# PosTrainer

## Topics
* [What is PosTrainer?](#what-is-postrainer)
* [Can I use the code?](#can-i-use-code-from-this-repo)
* [Learning Sources/Inspiration](#sources-and-inspiration)
* [Support Me](#contactsupport-me)
* [License](#license)

## What is PosTrainer?

PosTrainer is an Notification adn Exercise instruction App which allows the user to set daily reminders to sit up Straight and/or Perform stretches/exercises to combat bad Posture from being a Desk Jockey. 

This App is available on the Play Store here (if you learn something from the code, consider downloading and rating the app):
https://play.google.com/store/apps/details?id=com.bracketcove.postrainer

### Reminders Component
<img src="pos_screen_reminders.jpg" alt="Reminder List Screen" width="270" height="480"/>


### Reminder Detail Component
<img src="reminder_detail.png" alt="Reminder Detail Screen" width="270" height="480"/>

### Movements Component
<img src="pos_screen_movement_list.jpg" alt="Movement List Screen" width="270" height="480"/>

### Movement Detail Component
<img src="pos_screen_movement_detail_image.jpg" alt="Movement Detail Screen" width="270" height="480"/>

<img src="pos_screen_movement_detail_info.jpg" alt="Movement Detail Screen" width="270" height="480"/>

### Settings  Component
<img src="settings.png" alt="Settings Screen" width="270" height="480"/>


## Can I use code from this Repo?
Absolutely, pursuant to the project's [LICENSE](LICENSE.md). I primarily learned how to build Profiler by learning from free sources and referencing open source libraries, many of which I list (here)[#sources]. That being said, the logo (whenever I get around to making it) and name are my intellectual creations, so don't use them unless you are linking/reffering to this Repo.

Follow the rules in the license, and you're good.

## APIs and Architectures Used:
- Android Jetpack/Components: Navigation, ViewModel, LiveData, ConstraintLayout
- Realm for local Persistence
- BroadcastReceiver, AlarmManager, ForegroundService, NotificationManager

The Architecture is my own personal style which I have developed after eventually deciding that I was done calling things names that
did not make much sense to me. That is not to say my names are better, but it is to say that they make more sense to me. If you are actually
interested in learning what Software Architecture means to me without making things needlessly vague nor complex, please read this article:

https://medium.com/datadriveninvestor/programming-fundamentals-part-5-separation-of-concerns-software-architecture-f04a900a7c50

## Sources and Inspiration

Fernando Cejas' Repository and material on Android and Clean Architecture was very useful to me. Being able to see working code is one of the greatest resources, and although I do many things differently (differently, not necessarily better), I don't think I would attempted this without such an awesome reference!

https://github.com/android10/Android-CleanArchitecture

Uncle Bob (Robert C. Martin) explains Clean Architecture in a very practicle and Framework Independent way. You'll want to listen when he speaks on Software Architecture :).

https://www.youtube.com/watch?v=Nsjsiz2A9mg&t=1832s

Martin Fowler eventually broke MVP Architecture into two sub-styles. I currently try to employ "Passive View" as best I can

https://martinfowler.com/eaaDev/PassiveScreen.html

Donn and Kaushik from Fragmented Podcast did an episode on Clean, which also helped my understand a fair bit. Also these guys do an awesome podcast so you should watch it anyway:

http://fragmentedpodcast.com/episodes/11/



## Contact/Support me:
It's my personal goal to create free high-quality content, accesible by anyone who has an Internet connection, as I don't feel ownership over knowledge which has graciously been given to me for free (I've never taken a paid course for Android Development).

That being said, if the 200+ hours of my time spent coding, researching, making learning aids, and producing content for this project, and the effort I take to explain things in a simple and clear way (which I often fail to do), is worth throwing a bit of money at, then please do!

If you're in a position where monetary support isn't an option, then you can also help out by liking/sharing my stuff on Social Media. This helps me with SEO and building an audience, and I greatly appreciate it! Lastly, drop me a comment on one of my videos.

[G+](https://plus.google.com/+wiseass)

[Facebook](https://www.facebook.com/wiseassblog/)

[Twitter](https://twitter.com/wiseAss301)

[Primary Website](http://wiseassblog.com/)

## License
 * Copyright 2016, The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
