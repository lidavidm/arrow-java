<!---
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->

# How to contribute to Apache Arrow Java

## Did you find a bug?

The Arrow Java project uses GitHub as a bug tracker.  To report a bug, sign in
to your GitHub account, navigate to [GitHub issues](https://github.com/apache/arrow-java/issues)
and click on **New issue** .

Before you create a new bug entry, we recommend you first search among
existing Arrow issues in [GitHub](https://github.com/apache/arrow-java/issues).

## Did you write a patch that fixes a bug or brings an improvement?

- Create a GitHub issue and submit your changes as a GitHub Pull Request.
- [Reference the issue](https://docs.github.com/en/issues/tracking-your-work-with-issues/using-issues/linking-a-pull-request-to-an-issue#linking-a-pull-request-to-an-issue-using-a-keyword) in your PR description.
- Add one or more of the labels "bug-fix", "dependencies", and "enhancement" to your PR as appropriate.
  - "bug-fix" is for PRs that fix a bug.
  - "dependencies" is for PRs that upgrade a dependency or other administrative work (build system, release process, etc.).
  - "enhancement" is for PRs that add new features.
- Add the "breaking-change" label to your PR if there are breaking API changes.
- Add the PR title. The PR title will be used as the eventual commit message, so please make it descriptive but succinct.

Example #1:

```
GH-12345: Document the pull request process

Explain how to open a pull request and what the title, body, and labels should be.

Closes #12345.

Labels: dependencies
```

Example #2:

```
GH-42424: Expose Netty server builder in Flight

Allow direct usage of gRPC APIs for low-level control.

Closes #42424.

Labels: enhancement
```

### Minor Fixes

Any functionality change should have a GitHub issue opened. For minor changes that
affect documentation, you do not need to open up a GitHub issue. Instead you can
prefix the title of your PR with "MINOR: " if it meets one of the following:

*  Grammar, usage and spelling fixes that affect no more than 2 files
*  Documentation updates affecting no more than 2 files and not more
   than 500 words.

## Do you want to propose a significant new feature or an important refactoring?

We ask that all discussions about major changes in the codebase happen
publicly on the [arrow-dev mailing-list](https://mail-archives.apache.org/mod_mbox/arrow-dev/).

## Do you have questions about the source code, the build procedure or the development process?

You can also ask on the mailing-list, see above.

## Further information

Please read our [development documentation](https://arrow.apache.org/docs/developers/index.html)
or look through the [New Contributor's Guide](https://arrow.apache.org/docs/developers/guide/index.html).
