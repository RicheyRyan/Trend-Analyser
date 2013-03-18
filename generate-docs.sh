#!/bin/bash
lein marg ./src/trend_analyser/handler.clj ./src/trend_analyser/tweets.clj ./src/trend_analyser/html.clj ./src/trend_analyser/data.clj ./src/trend_analyser/scheduler.clj ./project.clj
ditto ./docs/uberdoc.html ./public
