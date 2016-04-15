Name            : demo-project
Summary         : Demo Project
Version         : 1.0

Release         : %{snapshot}%{?dist}

Group           : Applications/Internet
License         : Public domain

BuildArch       : noarch
BuildRoot       : %{_tmppath}/%{name}-buildroot

# Use "Requires" for any dependencies, for example:
#Requires        : httpd

# Description gives information about the rpm package. This can be expanded up to multiple lines.
%description
Basic test package


# Prep is used to set up the environment for building the rpm package
# Expansion of source tar balls are done in this section
%prep

# Used to compile and to build the source
%build

# house keeping script before install
%pre
mkdir -p /opt/test/

# The installation.
# We actually just put all our install files into a directory structure that mimics a server directory structure here
%install
rm -rf "$RPM_BUILD_ROOT"
install -d -m 755 "$RPM_BUILD_ROOT/opt/test/"
cp -R ../SOURCES/* "$RPM_BUILD_ROOT/opt/test/"

# Contains a list of the files that are part of the package
# See useful directives such as attr here: http://www.rpm.org/max-rpm-snapshot/s1-rpm-specref-files-list-directives.html
%files
/opt/test/

# Used to store any changes between versions
%changelog
