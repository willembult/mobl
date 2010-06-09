#!/bin/bash

jekyll
rsync -avz -e ssh www/* zef@zerver2.zef.me:www/mobl-lang.org/
