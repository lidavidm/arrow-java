// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

module.exports = {
    check_title_format: function({core, github, context}) {
        const title = context.payload.pull_request.title;
        if (title.startsWith("MINOR: ")) {
            context.log("PR is a minor PR");
            return {"issue": null};
        }

        const match = title.match(/^GH-([0-9]+): .*$/);
        if (match === null) {
            core.setFailed("Invalid PR title format. Must either be MINOR: or GH-NNN:");
            return {"issue": null};
        }
        return {"issue": parseInt(match[1], 10)};
    },

    apply_labels: async function({core, github, context}) {
        const body = (context.payload.pull_request.body || "").split(/\n/g);
        var has_breaking = false;
        for (const line of body) {
            if (line.trim().startsWith("**This contains breaking changes.**")) {
                has_breaking = true;
                break;
            }
        }
        if (has_breaking) {
            console.log("PR has breaking changes");
            await github.rest.issues.addLabels({
                issue_number: context.payload.pull_request.number,
                owner: context.repo.owner,
                repo: context.repo.repo,
                labels: ["breaking-change"],
            });
        } else {
            console.log("PR has no breaking changes");
        }
    },

    check_labels: async function({core, github, context}) {
        const categories = ["bug-fix", "chore", "dependencies", "documentation", "enhancement"];
        const labels = (context.payload.pull_request.labels || []);
        const required = new Set(categories);
        var found = false;

        for (const label of labels) {
            console.log(`Found label ${label.name}`);
            if (required.has(label.name)) {
                found = true;
                break;
            }
        }

        if (found) {
            console.log("PR has appropriate label(s)");
        } else {
            console.log("PR has is missing label(s)");
            console.log("Label the PR with one or more of:");
            for (const label of categories) {
                console.log(`- ${label}`);
            }
            console.log();
            console.log("Also, add 'breaking-change' if appropriate.");
            console.log("See CONTRIBUTING.md for details.");
            core.setFailed("Missing required labels.  See CONTRIBUTING.md");
        }
    },

    check_linked_issue: async function({core, github, context, issue}) {
        console.log(issue);
        if (issue.issue === null) {
            console.log("This is a MINOR PR");
            return;
        }
        const expected = `https://github.com/apache/arrow-java/issues/${issue.issue}`;

        const query = `
query($owner: String!, $name: String!, $number: Int!) {
  repository(owner: $owner, name: $name) {
    pullRequest(number: $number) {
      closingIssuesReferences(first: 50) {
        edges {
          node {
            number
          }
        }
      }
    }
  }
}`;

        const result = await github.graphql(query, {
            owner: context.repo.owner,
            name: context.repo.repo,
            number: context.payload.pull_request.number,
        });
        const issues = result.repository.pullRequest.closingIssuesReferences.edges;
        console.log(issues);

        for (const link of issues) {
            console.log(`PR is linked to ${link.node.number}`);
            if (link.node.number === issue.issue) {
                console.log(`Found link to ${expected}`);
                return;
            }
        }
        console.log(`Did not find link to ${expected}`);
        core.setFailed("Missing link to issue in title");
    },
};
