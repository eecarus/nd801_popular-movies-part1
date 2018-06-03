# Popular Movies Part 1

## Project Information

This project is being built as a submission for a Udacity Android Developer Nanodegree in conjunction with a Grow with Google Scholarship.

## Project Overview (copied from Udacity Github site)

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing. We will split the development of this app in two stages. First, let's talk about stage 1.

In this stage you’ll build the core experience of your movies app.

You app will:

- Present the user with a grid arrangement of movie posters upon launch.
- Allow your user to change sort order via a setting.  The sort order can be by most popular or by highest-rated
- Allow the user to tap on a movie poster and transition to a details screen with additional information such as: original title, movie poster image thumbnail, A plot synopsis (called overview in the api), user rating (called vote_average in the api), release date

## Why this Project

To become an Android developer, you must know how to bring particular mobile experiences to life. Specifically, you need to know how to build clean and compelling user interfaces (UIs), fetch data from network services, and optimize the experience for various mobile devices. You will hone these fundamental skills in this project.

By building this app, you will demonstrate your understanding of the foundational elements of programming for Android. Your app will communicate with the Internet and provide a responsive and delightful user experience.

## What Will I Learn?

- You will fetch data from the Internet with theMovieDB API.
- You will use adapters and custom list layouts to populate list views.
- You will incorporate libraries to simplify the amount of code you need to write

## Important setup information
- In order to avoid exposing an API key to github, I chose to use a resource file that I have added to my gitignore file.  This file is called `secrets.xml`.  In it, this code expects a property called `api_moviedb_api_key` that contains a valid MovieDB API token.  The code loads the key before making API calls.

## Attributions and references

- [Storing Secret Keys in Android](https://github.com/codepath/android_guides/wiki/Storing-Secret-Keys-in-Android)
- [Environmental variables, API key and secret, BuildConfig and Android Studio](http://www.rainbowbreeze.it/environmental-variables-api-key-and-secret-buildconfig-and-android-studio/)
- [Securely Storing Secrets in an Android Application using the KeyStore API](https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3)
- [Hiding Secrets in Android Apps](https://rammic.github.io/2015/07/28/hiding-secrets-in-android-apps/)
- [RecyclerViews](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [Picasso and RecyclerViews](https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Picasso-Library)

## Concepts for this project

- Learn to use RecyclerView, ViewHolder and Adapter classes
- Note: 3 stock LayoutManagers for RecyclerView: LinearLayoutManager, GridLayoutManager, StaggeredGridLayoutManager
- Note: use setHasFixedSize on RecyclerView to allow Android to optimize the view.
- Learn to use the different types of Intents: explicit intents and implicit intents. (Lesson 2.5)