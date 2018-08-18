package fr.houseofcode.dap.ws;

/**
 * @author djer
 */
//@EnableWebSecurity
public class SecurityConfig /* extends WebSecurityConfigurerAdapter */ {

//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().requestMatchers(PathRequest.toH2Console());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//       http.authorizeRequests().anyRequest().authenticated().and().requestCache()
//     .requestCache(new NullRequestCache()).and().httpBasic();
//
//        // standard MVC config
//        http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .anyRequest().authenticated().and().formLogin().permitAll();
//    }
//
////    @Bean
////    public HttpSessionIdResolver httpSessionIdResolver() {
////        return HeaderHttpSessionIdResolver.xAuthToken();
////    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user").password("{noop}password").roles("USER");
//    }

}
