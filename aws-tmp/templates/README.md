The files in this folder aim to help you work with the [Atom editor](https://atom.io/).

## Syntax highlighting
To use the `cfn` file extension with JSON syntax highlighting, edit the `config.cson` file to feature the following:

````
'*'
  core:
    customFileTypes:
      'source.json': [
        'cfn'
      ]
````

## Templates

Install the [`file-templates package`](https://atom.io/packages/file-templates) to enable templates.

You can now add the `*_template.cfn` files by:

1. Open a template file
1. Go to the `Packages`->`File Templates` menu and select `New template from this file`
1. Give your template a name
1. Create new template-based files by going to the `Packages`->`File Templates` menu and selecting `New file from template`.

## Snippets
You can add the contents of the `snippets.cson` file into your local snippets file. Check out `~/.atom/snippets.cson` or `Atom`->`Snippets`.
