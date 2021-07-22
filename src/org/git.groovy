package org

def checkOutFrom(repo) {
  git url: "git@github.com:chandana48/${repo}"
}

return this
