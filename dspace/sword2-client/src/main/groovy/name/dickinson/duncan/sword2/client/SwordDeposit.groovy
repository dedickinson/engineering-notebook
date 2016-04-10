package name.dickinson.duncan.sword2.client
import org.apache.commons.validator.routines.UrlValidator
import org.swordapp.client.AuthCredentials
import org.swordapp.client.DepositFactory
import org.swordapp.client.SWORDClient


class SwordDeposit {

	static private def cli
	static private depfactory = new DepositFactory()

	static {
		cli = new CliBuilder(usage:'SwordDeposit.groovy [options] <url>')
		cli.with{
			h longOpt: 'help', 'Usage Information', required: false
			u longOpt: 'user', args:1, argName:'username', 'The Dspace username', required: false
			p longOpt: 'pass', args:1, argName:'password', 'The DSpace password', required: false
			f longOpt: 'file', args:1, argName:'zip file', 'The zip file to be ingested', required: true
			_ longOpt: 'url', args:1, argName:'url', 'Set HTTP or HTTPS URL to work with',required: true
		}
	}

	static public checkUrl(url){
		def schemes = ["http", "https"]
		def urlValidator = new UrlValidator((String[])schemes, UrlValidator.ALLOW_LOCAL_URLS)
		urlValidator.isValid(url)
	}

	static getMd5Hex(file){
		assert new File(file).exists()
		def fis= new FileInputStream(file)
		org.apache.commons.codec.digest.DigestUtils.md5Hex(fis)
	}

	static public depositMetsZip(url, zipfile, user = '', pass = '') {
		//Preconditions
		assert SwordDeposit.checkUrl(url)
		assert new File(zipfile).exists()

		//Body
		def client = new SWORDClient()
		def auth = new AuthCredentials(user,pass)
		def instream = new FileInputStream(zipfile)
		def dep = depfactory.newBinaryOnly(instream,
				zipfile,
				'application/zip',
				'http://purl.org/net/sword/package/METSDSpaceSIP',
				'This is not a good SLUG',
				getMd5Hex(zipfile),
				false)

		client.deposit url, dep, auth
	}

	static public main(args) {

		def options = cli.parse(args)

		if (!options) {
			return
		}

		if (options.h){
			cli.usage()
			return
		}

		def user = ''
		if (options.user){
			user = options.user
		}

		def password = ''
		if (options.pass){
			password = options.pass
		}

		println options.url

		try {
			def rcpt = depositMetsZip(options.url, options.f, user, password)
		} catch(e) {
			println "An exception has occurred: ${e.getMessage()}"
		}
	}
}
