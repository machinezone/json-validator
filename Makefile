# vim: ts=4 sts=4 sw=4 noexpandtab

# Run unit tests
check: gradle-clean gradle
	./gradlew :test -b build.gradle

# Clean temporary files
clean: gradle-clean
	rm -rf classes/

# Remove all the generated files even if they aren't temporary
distclean: clean
	rm -f gradlew gradlew.bat
	rm -rf .gradle/ .idea/ gradle/

#
# ==== Private functions ====
#

gradle-clean:
	@[ ! -d build -o -w build ] || (echo "Directory 'build' isn't writable" && exit 2)
	rm -rf build/

gradle:
	gradle wrapper
