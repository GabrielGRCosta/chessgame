# Commit Conventions

## Summary

This is a simplified version of [Conventional Commits v1.0.0](https://www.conventionalcommits.org/en/v1.0.0/), which includes only the minimal necessary for this project.

## Message format

```
<type>: <description>

[optional body]

[optional footer]
```

### Type

* **feat**: A new feature
* **fix**: A bug fix
* **docs**: Documentation only changes
* **build**: Changes that affect the build system or external dependencies
* **perf**: A code change that improves performance
* **refactor**: A code change that neither fixes a bug nor adds a feature
* **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
* **test**: Adding missing tests or correcting existing tests
* **revert**: Reverting a previous commit. Description must be the header of the reverted commit. Body must say `This reverts commit <hash>.`, where hash is the SHA of the commit being reverted

### Description

A straight forward description of the change.

* Use the imperative, present tense: "change" not "changed" nor "changes"
* Don't capitalize the first letter
* No dot (.) at the end

### Body

A more detailed description of the change.

### Footer

Any information about Breaking Changes.

* Begin with `BREAKING CHANGE:` followed by a space.

## Examples

```
docs: correct typos of README
```
```
fix: prevent mouseEvent overflow
```
```
feat: add string parser

BREAKING CHANGE: requires Java 11 or greater.
```
```
build: include JavaFX repository

It will allow for more flexible GUI programming.
```
```
revert: fix: prevent mouseEvent overflow

This reverts commit 843d074.
```
