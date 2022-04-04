from selenium import webdriver

from selenium.webdriver.common.keys import Keys

import time

import warnings
warnings.filterwarnings("ignore")

#윈도우용 크롬 웹드라이버 실행 경로(windows)지정
browser=webdriver.Chrome("./chromedriver.exe")
browser.get("https://www.k-auction.com/")

browser.implicitly_wait(15)

#메뉴 클릭
browser.find_element_by_css_selector('div.d-xl-none.nav-menu-toggler.p-l-10.m-r-10').click()

oneline=browser.find_element_by_css_selector('#header > div.header-inner > div > ul > li:nth-child(3) > a')
oneline.click()

auction=browser.find_element_by_css_selector('#header > div.header-inner > div > ul > li:nth-child(3) > ul > li > ul > li:nth-child(1) > a').click()
    
for number in range(1,11):
    print(browser.find_elements_by_css_selector(f"#list > div:nth-child({number}) > div")[0].text)
    browser.execute_script("window.scrollTo(0, document.body.scrollHeight)") 
    
    browser.find_element_by_xpath("//li[@class='paginate_button page-item active']/following-sibling::li").click()
    
    time.sleep(2)
            
browser.quit()
