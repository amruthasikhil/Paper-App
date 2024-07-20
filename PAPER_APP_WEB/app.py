import base64
import datetime
import re

from flask import Flask, render_template, request, session, jsonify
from dbconnection import Db

app = Flask(__name__)
app.secret_key = "sr"


@app.route('/')
def hello_world():
    return index_login()

# //POST method//
@app.route('/login_post', methods=['POST'])
def login_post():
    username = request.form['textfield1']
    password = request.form['textfield2']
    db = Db()
    qry = "SELECT * FROM login WHERE user_name='"+username+"' AND `password`='"+password+"'"
    res = db.selectOne(qry)
    if res is None:
        return "<script>alert('Login Failed. Please enter a valid Username and Password.');window.location='/'</script>"
    elif res["type"] == "admin":
        session['lid'] = res['login_id']
        return index_admin()
    elif res["type"] == "provider":
        session['lid'] = res['login_id']
        return index_provider()
    elif res["type"] == "agent":
        session['lid'] = res['login_id']
        return index_agent()
    else:
        return "ok"


# //GET method//
@app.route('/index_login')
def index_login():
    return render_template('index_login.html')


# //GET method//
@app.route('/index_admin')
def index_admin():
    return render_template('index_admin.html')


# //GET method//
@app.route('/index_provider')
def index_provider():
    return render_template('index_provider.html')


# //GET method//
@app.route('/index_agent')
def index_agent():
    return render_template('index_agent.html')


# //GET method//
@app.route('/index_registration_provider')
def index_registration_provider():
    return render_template('index_registration_provider.html')


# //GET method//
@app.route('/index_registration_agent')
def index_registration_agent():
    return render_template('index_registration_agent.html')


##############################################################################################
##############################################################################################
# //ADMIN MODULE//WEB PORTION//

# //GET method//
@app.route('/admin_home')
def admin_home():
    return render_template('admin/admin_home.html')


# //GET method//
@app.route('/admin_view_provider_request')
def admin_view_provider_request():
    pr = Db()
    qry = "SELECT * FROM login , provider WHERE provider.provider_login_id = login.login_id AND login.type = 'pending'"
    res = pr.select(qry)
    return render_template('admin/admin_view_provider_request.html', data=res)


# // APPROVE //
@app.route('/provider_approve/<id>')
def provider_approve(id):
    rs = Db()
    qry1 = " UPDATE login SET TYPE ='provider' WHERE login_id ='"+id+"' "
    rs.update(qry1)

    return "<script>window.location='/admin_view_provider_request'; alert('Provider Approved Successfully.')</script>"


# // REJECT //
@app.route('/provider_reject/<id>')
def provider_reject(id):
    rs = Db()
    qry1 = " UPDATE login SET TYPE ='rejected' WHERE login_id ='"+id+"' "
    rs.update(qry1)
    return "<script>window.location='/admin_view_provider_request';alert('Provider Request Rejected.')</script>"


# //GET method//
@app.route('/admin_view_agent_request')
def admin_view_agent_request():
    ab = Db()
    qry = " SELECT * FROM `login`, `agent` WHERE `login`.`login_id` = `agent`.`agent_login_id` AND `login`.`type` = 'pending'"
    res = ab.select(qry)
    return render_template('admin/admin_view_agent_request.html', data=res)


# // APPROVE //
@app.route('/agent_approve/<id>')
def agent_approve(id):
    rs = Db()
    qry1 = "update login set type ='agent' where login_id ='"+id+"'"
    rs.update(qry1)
    return "<script>window.location='/admin_view_agent_request';alert('Agent Approved Successfully.')</script>"


# // REJECT //
@app.route('/agent_reject/<id>')
def agent_reject(id):
    rs = Db()
    qry1 = "UPDATE login SET TYPE ='rejected' WHERE login_id ='"+id+"'"
    rs.update(qry1)
    return "<script>window.location='/admin_view_agent_request';alert('Agent Request Rejected.')</script>"

# //GET method//
@app.route('/admin_view_approved_providers')
def admin_view_approved_providers():
    db = Db()
    qry = "SELECT provider.* FROM provider, login WHERE login.type = 'provider' " \
          "AND login.login_id = provider.provider_login_id"
    res = db.select(qry)
    return render_template('admin/admin_view_approved_providers.html', data=res)


# //GET method//
@app.route('/admin_view_approved_agents')
def admin_view_approved_agents():
    db = Db()
    qry = "SELECT agent.* FROM agent, login WHERE login.type = 'agent' " \
          "AND login.login_id = agent.agent_login_id"
    res = db.select(qry)
    return render_template('admin/admin_view_approved_agents.html', data=res)


# //GET method//
@app.route('/admin_view_complaints')
def admin_view_complaints():
    db = Db()
    qry = "SELECT complaints.*, user.*, agent.agent_name FROM user, agent, complaints " \
          "WHERE user.user_login_id = complaints.user_id AND complaints.agent_login_id = agent.agent_login_id"
    res = db.select(qry)
    return render_template('admin/admin_view_complaints.html', data=res)


# //GET method//
@app.route('/admin_change_username_password')
def admin_change_username_password():
    return render_template('admin/admin_change_username_password.html')


# //post method//
@app.route('/admin_change_username_password_post', methods=['POST'])
def admin_change_username_password_post():
    db = Db()
    username1 = request.form['textfield1']
    password1 = request.form['textfield2']
    username2 = request.form['textfield3']
    password2 = request.form['textfield4']
    qry1 = "SELECT * FROM login WHERE user_name ='"+username1+"' AND password ='"+password1+"' AND login_id ='"+str(session['lid'])+"' "
    res1 = db.selectOne(qry1)
    if res1 is not None:
        qry2 = " UPDATE login SET user_name ='"+username2+"', password ='"+password2+"'" \
               " WHERE login_id ='"+str(session['lid'])+"' "
        res2 = db.update(qry2)
    elif res1 is None:
        return "<script>alert('Failed. Invalid Username/Password Entered.'); window.location='/admin_change_username_password'</script>"
    else:
        return"<script> alert('Invalid Username or Password'); window.location='/admin_change_username_password' </script>"
    return 'Username/Password Updated Successfully.'


#######################################################################################################################
#######################################################################################################################
# //PROVIDER MODULE//WEB PORTION//

# //GET method//
@app.route('/provider_home')
def provider_home():
    return render_template('provider/provider_home.html')


# //GET method//
@app.route('/provider_registration')
def provider_registration():
    return render_template('provider/provider_registration.html')


# //POST method//
@app.route('/provider_registration_post', methods=['POST'])
def provider_registration_post():
    db = Db()
    name = request.form['textfield1']
    place = request.form['textfield2']
    pin = request.form['textfield3']
    phone = request.form['textfield4']
    email = request.form['textfield7']
    image = request.files['fileField']
    image.save("H:\\MY_SYSTEM_SOFTWARES_PROJECT\\FINAL_PROJECT_BCA_SURYA\\PAPER_APP_WEB\\static\\provider_image\\"+image.filename)
    path = "/static/provider_image/"+image.filename
    qry1 = " INSERT INTO login (user_name, PASSWORD, TYPE)" \
           " VALUES ('"+name+"','"+phone+"','pending')"
    res1 = db.insert(qry1)
    qry2 = " INSERT INTO provider (provider_name, phone, place, pin, email_id, provider_logo, provider_login_id)" \
           " VALUES ('"+name+"','"+phone+"','"+place+"','"+pin+"', '"+email+"','"+path+"','"+str(res1)+"')"
    res2 = db.insert(qry2)
    return 'Provider Registration Completed Successfully.'


# //GET method//
@app.route('/provider_profile_management')
def provider_profile_management():
    ab = Db()
    qry = " SELECT * FROM provider WHERE provider.provider_login_id='"+str(session['lid'])+"'"
    res = ab.selectOne(qry)
    return render_template('provider/provider_profile_management.html', data=res)


# //POST method//
@app.route('/provider_profile_management_post',methods=['POST'])
def provider_profile_management_post():
    bb = Db()
    name = request.form['textfield2']
    place = request.form['textfield3']
    pin = request.form['textfield4']
    phone = request.form['textfield5']
    if "filefield1" in request.files:
        image = request.files['filefield1']
        if image.filename == "":
            qry1 = " UPDATE provider" \
                   " SET provider_name ='"+name+"', phone ='"+phone+"', place='"+place+"', pin='"+pin+"'" \
                   " WHERE provider_login_id ='"+str(session['lid'])+"'"
            res1 = bb.update(qry1)
        else:
            image.save("H:\\MY_SYSTEM_SOFTWARES_PROJECT\\FINAL_PROJECT_BCA_SURYA\\PAPER_APP_WEB\\static\\provider_image\\"+image.filename)
            path = "/static/provider_image/"+image.filename
            qry2 = " UPDATE provider" \
                   " SET provider_name ='"+name+"', phone ='"+phone+"', place='"+place+"', pin ='"+pin+"', provider_logo ='"+path+"'" \
                   " WHERE provider_login_id ='"+str(session['lid'])+"'"
            res2 = bb.update(qry2)
    else:
        qry3 = " UPDATE provider" \
              " SET provider_name ='"+name+"', phone ='"+phone+"', place='"+place+"', pin='"+pin+"'" \
              " WHERE provider_login_id ='"+str(session['lid'])+"'"
        res3 = bb.update(qry3)
    return "<script>alert('Profile Updated Successfully.');window.location='/provider_profile_management'</script>"


# //GET method//
@app.route('/provider_magazine_management')
def provider_magazine_management():
    return render_template('provider/provider_magazine_management.html')


# //post method//
@app.route('/provider_magazine_management_post', methods=['POST'])
def provider_magazine_management_post():
    db = Db()
    magazine = request.form['textfield1']
    language = request.form['textfield2']
    price = request.form['textfield3']
    qry = " INSERT INTO magazines (magazine_name, provider_login_id, LANGUAGE, price)" \
          " VALUES ('"+magazine+"', '"+str(session['lid'])+"', '"+language+"', '"+price+"')"
    res = db.insert(qry)
    return "<script>window.location='/provider_magazine_management';alert('New Magazine Added.')</script>"


# //GET method//
@app.route('/provider_magazine_management_view')
def provider_magazine_management_view():
    db = Db()
    qry = " SELECT * FROM magazines WHERE provider_login_id = '"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_magazine_management_view.html', data=res)


# // EDIT //
@app.route('/provider_magazine_edit/<id>')
def provider_magazine_edit(id):
    db = Db()
    session['magazine_id'] = id
    qry = " SELECT * FROM magazines WHERE magazine_id= '"+id+"'"
    res = db.selectOne(qry)
    return render_template("provider/provider_magazine_edit.html", data=res)


# //EDIT POST method//
@app.route('/provider_magazine_edit_post', methods=['POST'])
def provider_magazine_edit_post():
    db = Db()
    x = session['magazine_id']
    magazine = request.form['textfield1']
    language = request.form['textfield2']
    price = request.form['textfield3']
    qry = " UPDATE magazines SET magazine_name ='"+magazine+"', LANGUAGE ='"+language+"', price ='"+price+"'" \
          " WHERE magazine_id ='"+str(x)+"'"
    res = db.update(qry)
    return "<script>window.location='/provider_magazine_management_view';alert('Magazine Updated Successfully.')</script>"


# //DELETE//
@app.route('/provider_magazine_delete/<id>')
def provider_magazine_delete(id):
    db = Db()
    qry = " DELETE FROM magazines WHERE magazine_id ='"+id+"'"
    db.delete(qry)
    return "<script>window.location='/provider_magazine_management_view';alert('Magazine Deleted Successfully.')</script>"


# /GET method//
@app.route('/provider_edition_management')
def provider_edition_management():
    return render_template('provider/provider_edition_management.html')


# /POST method//
@app.route('/provider_edition_management_post', methods=['POST'])
def provider_edition_management_post():
    db = Db()
    place = request.form['textfield1']
    qry = " INSERT INTO editions (edition_place, provider_login_id) VALUES ('"+place+"', '"+str(session['lid'])+"')"
    res = db.insert(qry)
    return "<script>window.location='/provider_edition_management';alert('New Edition Added.')</script>"


# /GET method//
@app.route('/provider_edition_management_view')
def provider_edition_management_view():
    db = Db()
    qry = " SELECT provider.provider_login_id, editions.* FROM provider, editions" \
          " WHERE provider.provider_login_id = editions.provider_login_id" \
          " AND provider.provider_login_id ='"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_edition_management_view.html', data=res)


# // EDIT //
@app.route('/provider_edition_edit/<id>')
def provider_edition_edit(id):
    db = Db()
    session['edition_id'] = id
    qry = "SELECT * FROM editions WHERE edition_id ='"+id+"'"
    res = db.selectOne(qry)
    return render_template("provider/provider_edition_edit.html", data=res)


# //EDIT POST method//
@app.route('/provider_edition_edit_post', methods=['POST'])
def provider_edition_edit_post():
    db = Db()
    b1 = session['edition_id']
    edition_name = request.form['textfield1']
    qry = " UPDATE editions SET edition_place = '"+edition_name+"' WHERE edition_id ='"+str(b1)+"'"
    print(qry)
    res = db.update(qry)
    return "<script>window.location='/provider_edition_management_view';alert('Edition Updated Successfully.')</script>"


# // DELETE //
@app.route('/provider_edition_delete/<id>')
def provider_edition_delete(id):
    db = Db()
    qry = "DELETE FROM editions WHERE edition_id ='"+id+"'"
    db.delete(qry)
    return "<script>window.location='/provider_edition_management_view';alert('Edition Deleted Successfully.')</script>"


# /GET method//
@app.route('/provider_edition_language_management')
def provider_edition_language_management():
    db = Db()
    qry = " SELECT * FROM editions WHERE provider_login_id = '"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_edition_language_management.html', data=res)


# /POST method//
@app.route('/provider_edition_language_management_post', methods=['POST'])
def provider_edition_language_management_post():
    db = Db()
    edition = request.form['edition']
    language = request.form['textfield2']
    qry = " INSERT INTO edition_languages (edition_language, edition_id)" \
          " VALUES ('"+language+"','"+edition+"')"
    res = db.insert(qry)
    return "<script>window.location='/provider_edition_language_management';alert('New Edition Language Added.')</script>"


# /GET method//
@app.route('/provider_edition_language_management_view')
def provider_edition_language_management_view():
    db = Db()
    qry = " SELECT editions.edition_place, edition_languages.*" \
          " FROM editions, edition_languages, provider " \
          " WHERE edition_languages.edition_id = editions.edition_id" \
          " AND editions.provider_login_id = provider.provider_login_id " \
          " AND editions.provider_login_id = '"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_edition_language_management_view.html', data=res)


# // EDIT //
@app.route('/provider_edition_language_edit/<id>')
def provider_edition_language_edit(id):
    db = Db()
    session['language_id'] = id
    qry1 = " SELECT * FROM editions" \
           " WHERE provider_login_id = '"+str(session['lid'])+"'"
    res1 = db.select(qry1)
    qry = " SELECT editions.edition_id AS eid, editions.edition_place, edition_languages.*" \
          " FROM editions, edition_languages" \
          " WHERE editions.edition_id=edition_languages.edition_id " \
          " AND editions.provider_login_id = '"+str(session['lid'])+"'" \
          " AND edition_languages.language_id='"+id+"'"
    res = db.selectOne(qry)
    print(qry)
    return render_template("provider/provider_edition_language_edit.html", data=res, data1=res1)


# //EDIT POST method//
@app.route('/provider_edition_language_edit_post', methods=['POST'])
def provider_edition_language_edit_post():
    db = Db()
    a1 = session['language_id']
    edition = request.form['edition']
    language = request.form['textfield2']
    qry = " UPDATE edition_languages SET edition_language = '"+language+"', edition_id='"+edition+"'" \
          " WHERE language_id ='"+str(a1)+"'"
    res = db.update(qry)
    return "<script>alert('Edition Language Updated.');window.location='/provider_edition_language_management_view'</script>"


# //DELETE//
@app.route('/provider_edition_language_delete/<id>')
def provider_edition_language_delete(id):
    db = Db()
    qry = " DELETE FROM edition_languages WHERE language_id = '"+id+"'"
    db.delete(qry)
    return "<script>window.location='/provider_edition_language_management_view'; alert('Edition Language Deleted Successfully.')</script>"


# /GET method//
@app.route('/provider_notification_management')
def provider_notification_management():
    return render_template('provider/provider_notification_management.html')


# /POST method//
@app.route('/provider_notification_management_post', methods=['POST'])
def provider_notification_management_post():
    cd = Db()
    notification = request.form['textfield1']
    qry = " INSERT INTO notifications (notification, DATE, provider_login_id)" \
          " VALUES ('"+notification+"', curdate(),'"+str(session['lid'])+"' )"
    res = cd.insert(qry)
    return "<script>window.location='/provider_notification_management';alert('Notification Added Successfully...')</script>"


# /GET method//
@app.route('/provider_notification_management_view')
def provider_notification_management_view():
    db = Db()
    qry = " SELECT * FROM notifications WHERE provider_login_id = '"+str(session['lid'])+"' "
    res = db.select(qry)
    return render_template('provider/provider_notification_management_view.html', data=res)


# //EDIT//
@app.route('/provider_notification_edit/<id>')
def provider_notification_edit(id):
    db = Db()
    session['notification_id'] = id
    qry = " SELECT * FROM notifications WHERE notification_id = '"+id+"' "
    res = db.selectOne(qry)
    return render_template("provider/provider_notification_edit.html", data=res)


# //EDIT POST method//
@app.route('/provider_notification_edit_post', methods=['POST'])
def provider_notification_edit_post():
    db = Db()
    x = session['notification_id']
    notification = request.form['textfield1']
    qry = " UPDATE notifications SET notification = '"+notification+"' WHERE notification_id = '"+str(x)+"'"
    res = db.update(qry)
    return "<script>alert('Notification Updated Successfully...');window.location='/provider_notification_management_view'</script>"


# //DELETE//
@app.route('/provider_notification_delete/<id>')
def provider_notification_delete(id):
    db = Db()
    qry = " DELETE FROM notifications WHERE notification_id = '"+id+"'"
    db.delete(qry)
    return "<script>window.location='/provider_notification_management_view'; alert('Notification Deleted Successfully.')</script>"


# //GET method, To get dropdown list//
@app.route('/provider_price_settings')
def provider_price_settings():
    db = Db()
    qry = "SELECT * FROM `editions` WHERE `provider_login_id`= '"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_price_settings.html', data=res)


@app.route('/ajax_edition_language',methods=['post'])
def ajax_edition_language():
    edid = request.form['edid']
    print(edid)
    db = Db()
    res = db.select("select * from edition_languages where edition_id='"+str(edid)+"'")
    print(res)
    return jsonify(res)


# /POST method//
@app.route('/provider_price_settings_post', methods=['POST'])
def provider_price_settings_post():
    db = Db()
    btn = request.form['button']
    edition = request.form.get('list1')
    language = request.form.get('list2')
    price = request.form['pr']
    if btn == "check":
        qry1 = "SELECT * FROM edition_languages WHERE edition_id = '"+edition+"' "
        res1 = db.select(qry1)
        print(res1)
        qry2 = "select  `editions`.* from `editions` where `provider_login_id`= '" + str(session['lid']) + "'"
        res2 = db.select(qry2)
        print(res2)
        return render_template("provider/provider_price_settings.html", value=res1, data=res2)
    if btn == "Submit":
        qry3 = " INSERT INTO price (edition_id, language_id, price, provider_login_id)" \
              " VALUES ('"+edition+"', '"+language+"', '"+price+"', '"+str(session['lid'])+"')"
        res = db.insert(qry3)
        return "<script>window.location='/provider_price_settings';alert('Price Added Successfully...')</script>"


# /GET method//
@app.route('/provider_price_settings_view')
def provider_price_settings_view():
    db = Db()
    qry = " SELECT price.*, editions.edition_place, edition_languages.edition_language" \
          " FROM price, editions, edition_languages" \
          " WHERE price.edition_id = editions.edition_id" \
          " AND price.language_id = edition_languages.language_id" \
          " AND price.provider_login_id = '"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_price_settings_view.html', data=res)


# //EDIT//
@app.route('/provider_price_edit/<id>')
def provider_price_edit(id):
    db = Db()
    session['price_id'] = id
    qry1 = "SELECT editions.edition_place FROM Price, editions WHERE price.edition_id = editions.edition_id AND price.price_id ='"+id+"'"
    res1 = db.selectOne(qry1)
    qry2 = " SELECT edition_languages.edition_language FROM Price, edition_languages WHERE price.language_id = edition_languages.language_id" \
           " AND price.price_id ='"+id+"'"
    res2 = db.selectOne(qry2)
    qry3 = " SELECT * FROM price WHERE price_id = '"+id+"'"
    res3 = db.selectOne(qry3)
    return render_template("provider/provider_price_edit.html", data1=res1, data2=res2, data3=res3)


# //EDIT POST method//
@app.route('/provider_price_edit_post', methods=['POST'])
def provider_price_edit_post():
    db = Db()
    x = session['price_id']
    price = request.form['pr']
    qry = " UPDATE price SET price = '"+price+"'" \
          " WHERE price_id = '"+str(x)+"'"
    res = db.update(qry)
    return "<script>window.location='/provider_price_settings_view';" \
           " alert('Price Updated Successfully...')</script>"


# //DELETE//
@app.route('/provider_price_delete/<id>')
def provider_price_delete(id):
    db = Db()
    qry = " DELETE FROM price" \
          " WHERE price_id = '"+id+"'"
    db.delete(qry)
    return "<script>window.location='/provider_price_settings_view';" \
           " alert('Price Deleted Successfully...')</script>"


# //GET method//
@app.route('/provider_view_agent_forwarded_paper_request')
def provider_view_agent_forwarded_paper_request():
    db = Db()
    qry = " SELECT agent.agent_name, editions.edition_place, edition_languages.edition_language, user.user_name, paper_requests.*" \
          " FROM agent, editions, edition_languages, USER, paper_requests" \
          " WHERE paper_requests.user_id = user.user_login_id" \
          " AND paper_requests.edition_id = editions.edition_id" \
          " AND paper_requests.agent_login_id = agent.agent_login_id" \
          " AND paper_requests.edition_language_id = edition_languages.language_id" \
          " AND paper_requests.status = 'forwarded' " \
          " AND editions.provider_login_id ='"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_view_agent_forwarded_paper_request.html', data=res)


# // APPROVE //
@app.route('/provider_approve_paper_request/<id>')
def provider_approve_paper_request(id):
    rs = Db()
    qry1 = "UPDATE paper_requests SET status ='approved' WHERE paper_request_id ='"+id+"'"
    rs.update(qry1)
    return "<script>window.location='/provider_view_agent_forwarded_paper_request';" \
           " alert('Paper Request Approved.')</script>"


# // REJECT //
@app.route('/provider_reject_paper_request/<id>')
def provider_reject_paper_request(id):
    rs = Db()
    qry = "UPDATE paper_requests SET status ='rejected' WHERE paper_request_id ='"+id+"'"
    rs.update(qry)
    return "<script>window.location='/provider_view_agent_forwarded_paper_request';" \
           " alert('Paper Request Rejected.')</script>"


# //GET method//
@app.route('/provider_view_agent_forwarded_magazine_request')
def provider_view_agent_forwarded_magazine_request():
    db = Db()
    qry = " SELECT magazine_requests.*, user.user_name, agent.agent_name, magazines.magazine_name, magazines.language" \
          " FROM magazine_requests, USER, agent, magazines" \
          " WHERE magazine_requests.user_id = user.user_login_id" \
          " AND  magazine_requests.magazine_id = magazines.magazine_id" \
          " AND magazine_requests.agent_login_id = agent.agent_login_id" \
          " AND magazine_requests.status = 'forwarded'" \
          " AND magazines.provider_login_id ='"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('provider/provider_view_agent_forwarded_magazine_request.html', data=res)


# // APPROVE //
@app.route('/provider_approve_magazine_request/<id>')
def provider_approve_magazine_request(id):
    rs = Db()
    qry = "UPDATE magazine_requests SET STATUS ='Approved', start_date = curdate() WHERE magazine_request_id ='"+id+"'"
    rs.update(qry)
    return "<script>window.location='/provider_view_agent_forwarded_magazine_request';" \
           " alert('Magazine Request Approved Successfully...')</script>"


# // REJECT //
@app.route('/provider_reject_magazine_request/<id>')
def provider_reject_magazine_request(id):
    rs = Db()
    qry = "UPDATE magazine_requests SET STATUS ='Rejected' WHERE magazine_request_id ='"+id+"'"
    rs.update(qry)
    return "<script>window.location='/provider_view_agent_forwarded_magazine_request';" \
           " alert('Magazine Request Rejected.')</script>"


# /GET method//
@app.route('/provider_agent_forwarded_classifieds_request')
def provider_agent_forwarded_classifieds_request():
    mv = Db()
    qry = " SELECT classifieds.*, user.user_name, agent.agent_name" \
          " FROM classifieds, USER, agent" \
          " WHERE classifieds.user_id = user.user_login_id" \
          " AND classifieds.agent_login_id = agent.agent_login_id" \
          " AND classifieds.status = 'forwarded'" \
          " AND classifieds.provider_login_id ='"+str(session['lid'])+"' "
    res = mv.select(qry)
    return render_template('provider/provider_agent_forwarded_classifieds_request.html', data=res)


# // APPROVE //
@app.route('/provider_approve_classifieds_request/<id>')
def provider_approve_classifieds_request(id):
    rs = Db()
    qry = "UPDATE classifieds SET STATUS ='approved' WHERE classified_id ='"+id+"'"
    rs.update(qry)
    return "<script>window.location='/provider_agent_forwarded_classifieds_request';" \
           " alert('Classifieds Request Approved.')</script>"


# // REJECT //
@app.route('/provider_reject_classifieds_request/<id>')
def provider_reject_classifieds_request(id):
    rs = Db()
    qry = "UPDATE classifieds SET STATUS ='rejected' WHERE classified_id ='"+id+"'"
    rs.update(qry)
    return "<script>window.location='/provider_agent_forwarded_classifieds_request';" \
           " alert('Classifieds Request Rejected.')</script>"


# //GET method//
@app.route('/provider_view_payment_informations')
def provider_view_payment_informations():
    return render_template('provider/provider_view_payment_informations.html')


# //POST method//
@app.route('/provider_view_payment_informations_search',methods=['post'])
def provider_view_payment_informations_search():
    db = Db()
    opt = request.form['cont']
    if opt == "Magazine":
        qry = " SELECT `user`.`user_name`,`agent`.`agent_name`,`payments`.`amount`,`payments`.`date` FROM `agent`,`payments`,`user`,`magazine_requests`,`magazines` "\
              " WHERE `magazines`.`magazine_id`=`magazine_requests`.`magazine_id` AND `magazines`.`provider_login_id`='"+str(session['lid'])+"'" \
              " AND `magazine_requests`.`agent_login_id`=`agent`.`agent_login_id`" \
              " AND `payments`.`user_id`=`user`.`user_login_id`AND `payments`.`request_id`=`magazine_requests`.`magazine_request_id`AND  `payments`.`type`='Magazine'"
        res = db.select(qry)
    elif opt == "Newspaper":
        qry = " SELECT `user`.`user_name`,`agent`.`agent_name`,`payments`.`amount`,`payments`.`date` FROM `agent`,`payments`,`user`,`paper_requests`,`editions`"\
              " WHERE `paper_requests`.`edition_id`=`editions`.`edition_id` AND `editions`.`provider_login_id`='"+str(session['lid'])+"'" \
              " AND `paper_requests`.`agent_login_id`=`agent`.`agent_login_id`"\
              " AND `paper_requests`.`user_id`=`user`.`user_login_id` AND `payments`.`request_id`=`paper_requests`.`paper_request_id`AND  `payments`.`type`='Newspaper'"
        res = db.select(qry)
    else:
        qry = "SELECT `user`.`user_name`,`agent`.`agent_name`,`payments`.`amount`,`payments`.`date` FROM `agent`,`payments`,`user`,`classifieds`"\
              " WHERE`payments`.`request_id`=`classifieds`.`classified_id` AND `classifieds`.`provider_login_id`='2'AND `classifieds`.`agent_login_id`=`agent`.`agent_login_id` AND"\
              " `payments`.`user_id`=`user`.`user_login_id` AND  `payments`.`type`='Classifieds'"
        res = db.select(qry)
    return render_template('provider/provider_view_payment_informations.html', data=res)


# //GET method//
@app.route('/provider_change_username_password')
def provider_change_username_password():
    return render_template('provider/provider_change_username_password.html')


# //post method//
@app.route('/provider_change_username_password_post', methods=['POST'])
def provider_change_username_password_post():
    db = Db()
    username1 = request.form['textfield1']
    password1 = request.form['textfield2']
    username2 = request.form['textfield3']
    password2 = request.form['textfield4']
    qry1 = "SELECT * FROM login WHERE user_name ='"+username1+"' AND password ='"+password1+"' AND login_id ='"+str(session['lid'])+"' "
    res1 = db.selectOne(qry1)
    if res1 is not None:
        qry2 = " UPDATE login SET user_name ='"+username2+"', password ='"+password2+"'" \
               " WHERE login_id ='"+str(session['lid'])+"' "
        res2 = db.update(qry2)
    else:
        return"<script> alert('Invalid Username or Password'); window.location='/provider_change_username_password' </script>"
    return 'ok'


#######################################################################################################################
#######################################################################################################################
# //AGENT MODULE//WEB PORTION//

# //GET method//
@app.route('/agent_home')
def agent_home():
    return render_template('agent/agent_home.html')


# //GET method//
@app.route('/agent_registration')
def agent_registration():
    return render_template('agent/agent_registration.html')


# //post method//
@app.route('/agent_registration_post', methods=['POST'])
def agent_registration_post():
    db = Db()
    name = request.form['textfield1']
    place = request.form['textfield2']
    district = request.form['select']
    pin = request.form['textfield4']
    phone = request.form['textfield5']
    email = request.form['email']
    photo = request.files['fileField']
    photo.save("H:\\MY_SYSTEM_SOFTWARES_PROJECT\\FINAL_PROJECT_BCA_SURYA\\PAPER_APP_WEB\\static\\agent_image\\"+photo.filename)
    path = "/static/agent_image/"+photo.filename
    qry1 = "INSERT INTO login (user_name, PASSWORD, TYPE) VALUES ('"+name+"','"+phone+"','pending')"
    res1 = db.insert(qry1)
    qry2 = " INSERT INTO agent (agent_name, agent_contact_number, agent_place, agent_email_id, agent_district, agent_pin, agent_image, agent_login_id)" \
           " VALUES ('"+name+"', '"+phone+"', '"+place+"', '"+email+"', '"+district+"', '"+pin+"', '"+path+"', '"+str(res1)+"' ) "
    res2 = db.insert(qry2)
    return "<script>window.location='/';alert('Agent Registration Completed Successfully.')</script>"


# //GET method//
@app.route('/agent_profile_management')
def agent_profile_management():
    ab = Db()
    qry = "SELECT * FROM agent WHERE agent.agent_login_id = '"+str(session['lid'])+"' "
    res = ab.selectOne(qry)
    return render_template('agent/agent_profile_management.html', data=res)


# //POST method//
@app.route('/agent_profile_management_post', methods=['POST'])
def agent_profile_management_post():
    pp = Db()
    name = request.form['textfield2']
    place = request.form['textfield3']
    district = request.form['textfield4']
    pin = request.form['textfield5']
    phone = request.form['textfield6']
    email = request.form['textfield7']
    if "filefield1" in request.files:
        print("yes")
        agent_image = request.files['filefield1']
        print(agent_image)
        if agent_image.filename == "":
            qry1 = " UPDATE agent SET agent_name ='"+name+"', agent_place='"+place+"', agent_district = '"+district+"'," \
                   " agent_pin = '"+pin+"', agent_contact_number = '"+phone+"', agent_email_id = '"+email+"' " \
                   " WHERE agent_login_id = '"+str(session['lid'])+"'"
            res = pp.update(qry1)
        else:
            agent_image.save("H:\\MY_SYSTEM_SOFTWARES_PROJECT\\FINAL_PROJECT_BCA_SURYA\\PAPER_APP_WEB\\static\\agent_image\\"+agent_image.filename)
            path = "/static/agent_image/"+agent_image.filename
            qry2 = " UPDATE agent SET agent_name ='"+name+"', agent_place='"+place+"', agent_district = '"+district+"'," \
                   " agent_pin = '"+pin+"', agent_contact_number = '"+phone+"', agent_email_id = '"+email+"', agent_image ='"+path+"' " \
                   " WHERE agent_login_id = '"+str(session['lid'])+"'"
            res1 = pp.update(qry2)
    else:
        print("no")
        qry3 = " UPDATE agent SET agent_name ='"+name+"', agent_place='"+place+"', agent_district = '"+district+"'," \
               " agent_pin = '"+pin+"', agent_contact_number = '"+phone+"', agent_email_id = '"+email+"' " \
               " WHERE agent_login_id = '"+str(session['lid'])+"'"
        res = pp.update(qry3)
    return "<script>window.location='/agent_profile_management';alert('Profile Updated Successfully.')</script>"


# //GET method//
@app.route('/agent_view_provider')
def agent_view_provider():
    pd = Db()
    qry = "SELECT * FROM provider"
    res = pd.select(qry)
    return render_template('agent/agent_view_provider.html', data=res)


# //GET method//
@app.route('/agent_view_newspaper_price_settings')
def agent_view_newspaper_price_settings():
    db = Db()
    qry = " SELECT `provider`.`provider_name`, `editions`.`edition_place`, `edition_languages`.`edition_language`, `price`.`price`" \
          " FROM `provider`, `editions`, `edition_languages`, `price`" \
          " WHERE `price`.`provider_login_id`=`provider`.`provider_login_id` AND `price`.`edition_id`=`editions`.`edition_id`" \
          " AND `price`.`language_id`=`edition_languages`.`language_id`"
    res = db.select(qry)
    return render_template('agent/agent_view_newspaper_price_settings.html', data=res)


# //GET method//
@app.route('/agent_view_magazine_price_settings')
def agent_view_magazine_price_settings():
    db = Db()
    qry = " SELECT `provider`.`provider_name`,`magazines`.* FROM `provider`, `magazines`" \
          " WHERE `magazines`.`provider_login_id`=`provider`.`provider_login_id`"
    res = db.select(qry)
    return render_template('agent/agent_view_magazine_price_settings.html', data=res)


# //GET method//
@app.route('/agent_view_subscribers_request')
def agent_view_subscribers_request():
    db = Db()
    # qry = " SELECT * FROM `login`, `user` WHERE `login`.`login_id` = `user`.`user_login_id` AND `login`.`type` = 'user'"
    qry = " SELECT * FROM `login`, `user`, subscriber WHERE `login`.`login_id` = `user`.`user_login_id`" \
          " AND user.user_login_id = subscriber.user_id" \
          " AND subscriber.agent_id = '"+str(session['lid'])+"'" \
          " AND subscriber.status = 'pending'"
    res = db.select(qry)
    return render_template('agent/agent_view_subscribers_request.html', data=res)


# // APPROVE //
@app.route('/subscriber_approve/<id>')
def subscriber_approve(id):
    rs = Db()
    qry = "update subscriber set status ='subscriber' where subscriber.user_id='" + id + "' and subscriber.agent_id='"+str(session['lid'])+"'"
    rs.update(qry)
    return "<script>window.location='/agent_view_subscribers_request'; alert('Subscriber Approved.')</script>"


# // REJECT //
@app.route('/subscriber_reject/<id>')
def subscriber_reject(id):
    rs = Db()
    qry = "update subscriber set status ='rejected' where subscriber.user_id='" + id + "' and subscriber.agent_id='"+str(session['lid'])+"'"
    rs.update(qry)
    return "<script>window.location='/agent_view_subscribers_request';alert('Subscriber Request Rejected.')</script>"


# //GET method//
@app.route('/agent_view_subscribers')
def agent_view_subscribers():
    db = Db()
    qry = " SELECT * FROM `login`, `user`, subscriber WHERE `login`.`login_id` = `user`.`user_login_id`" \
          " AND user.user_login_id = subscriber.user_id AND subscriber.agent_id = '"+str(session['lid'])+"'" \
          " AND subscriber.status = 'subscriber'"
    res = db.select(qry)
    return render_template('agent/agent_view_subscribers.html', data=res)


# //GET method//
@app.route('/agent_view_newspaper_request')
def agent_view_newspaper_request():
    db = Db()
    qry = " SELECT paper_requests.*, user.*, edition_languages.edition_language, editions.edition_place, provider.provider_name" \
          " FROM paper_requests, USER, edition_languages, editions, provider" \
          " WHERE paper_requests.agent_login_id ='"+str(session['lid'])+"'" \
          " AND user.user_login_id = paper_requests.user_id" \
          " AND paper_requests.status = 'pending'" \
          " AND paper_requests.edition_language_id = edition_languages.language_id" \
          " AND paper_requests.edition_id = editions.edition_id" \
          " AND editions.provider_login_id = provider.provider_login_id"
    res = db.select(qry)
    return render_template('agent/agent_view_newspaper_request.html', data=res)


# // FORWARD //
@app.route('/newspaper_request_forward/<id>')
def newspaper_request_forward(id):
    ab = Db()
    qry = " UPDATE paper_requests SET STATUS ='forwarded' WHERE paper_request_id ='"+id+"' "
    ab.update(qry)
    return "<script>window.location='/agent_view_newspaper_request'; alert('Request Forwarded Successfully.')</script>"


# // REJECT //
@app.route('/newspaper_request_reject/<id>')
def newspaper_request_reject(id):
    ab = Db()
    qry = "UPDATE paper_requests SET STATUS ='rejected' WHERE `paper_request_id`='"+id+"'"
    ab.update(qry)
    return "<script>window.location='/agent_view_newspaper_request';alert('Request Rejected.')</script>"


# //GET method//
@app.route('/agent_view_accepted_newspaper_request')
def agent_view_accepted_newspaper_request():
    db = Db()
    qry = " SELECT paper_requests.*, user.*, edition_languages.edition_language, editions.edition_place, provider.provider_name" \
          " FROM paper_requests, USER, edition_languages, editions, provider" \
          " WHERE paper_requests.agent_login_id ='"+str(session['lid'])+"'" \
          " AND user.user_login_id = paper_requests.user_id" \
          " AND paper_requests.status = 'approved'" \
          " AND paper_requests.edition_language_id = edition_languages.language_id" \
          " AND paper_requests.edition_id = editions.edition_id" \
          " AND editions.provider_login_id = provider.provider_login_id"
    res = db.select(qry)
    return render_template('agent/agent_view_accepted_newspaper_request.html', data=res)


# //GET method//
@app.route('/agent_view_magazine_request')
def agent_view_magazine_request():
    db = Db()
    qry = " SELECT provider.provider_name, magazines.*, magazine_requests.*, user.*" \
          " FROM  provider, magazines, magazine_requests, USER" \
          " WHERE magazine_requests.status = 'pending'" \
          " AND provider.provider_login_id = magazines.provider_login_id" \
          " AND magazine_requests.magazine_id = magazines.magazine_id " \
          " AND magazine_requests.user_id = user.user_login_id" \
          " AND magazine_requests.agent_login_id ='"+str(session['lid'])+"' "
    res = db.select(qry)
    return render_template('agent/agent_view_magazine_request.html', data=res)


# // FORWARD //
@app.route('/magazine_request_forward/<id>')
def magazine_request_forward(id):
    ab = Db()
    qry = " UPDATE magazine_requests SET STATUS ='forwarded' WHERE magazine_request_id ='"+id+"' "
    ab.update(qry)
    return "<script>window.location='/agent_view_magazine_request'; alert('Request Forwarded Successfully.')</script>"


# // REJECT //
@app.route('/magazine_request_reject/<id>')
def magazine_request_reject(id):
    ab = Db()
    qry = "UPDATE magazine_requests SET STATUS ='rejected' WHERE `magazine_request_id`='"+id+"'"
    ab.update(qry)
    return "<script>window.location='/agent_view_magazine_request';alert('Request Rejected Successfully.')</script>"


# //GET method//
@app.route('/agent_view_accepted_magazine_request')
def agent_view_accepted_magazine_request():
    db = Db()
    qry = " SELECT provider.provider_name, magazines.*, magazine_requests.*, user.*" \
          " FROM  provider, magazines, magazine_requests, USER" \
          " WHERE magazine_requests.status = 'approved'" \
          " AND provider.provider_login_id = magazines.provider_login_id" \
          " AND magazine_requests.magazine_id = magazines.magazine_id " \
          " AND magazine_requests.user_id = user.user_login_id" \
          " AND magazine_requests.agent_login_id ='" + str(session['lid']) + "'"
    res = db.select(qry)
    return render_template('agent/agent_view_accepted_magazine_request.html', data=res)


# //GET method//
@app.route('/agent_classifieds_request')
def agent_classifieds_request():
    db = Db()
    qry = " SELECT classifieds.*, user.*, `provider`.`provider_name` FROM `classifieds`, USER, `provider`" \
          " WHERE `classifieds`.`status`='pending' AND `provider`.`provider_login_id`=`classifieds`.`provider_login_id`" \
          " AND `user`.`user_login_id`=`classifieds`.`user_id` AND `classifieds`.`agent_login_id`='" + str(session['lid']) + "'"
    res = db.select(qry)
    return render_template('agent/agent_classifieds_request.html', data=res)


# // METHOD FOR FORWARD //
@app.route('/classifieds_forward/<id>')
def classifieds_forward(id):
    rs = Db()
    qry = "UPDATE `classifieds` SET STATUS ='forwarded' WHERE classified_id = '"+id+"'"
    rs.update(qry)
    return "<script>window.location='/agent_classifieds_request'; alert('Request Forwarded Successfully.')</script>"


# // METHOD FOR REJECT  //
@app.route('/classifieds_reject/<id>')
def classifieds_reject(id):
    rs = Db()
    qry = "UPDATE `classifieds` SET STATUS ='rejected' WHERE classified_id = '"+id+"'"
    rs.update(qry)
    return "<script>window.location='/agent_classifieds_request';alert('Request Rejected Successfully.')</script>"


# //GET method//
@app.route('/agent_accepted_classifieds_request')
def agent_accepted_classifieds_request():
    db = Db()
    qry = " SELECT classifieds.*, user.*, `provider`.`provider_name` FROM `classifieds`, USER, `provider`" \
          " WHERE `classifieds`.`status`='approved' AND `provider`.`provider_login_id`=`classifieds`.`provider_login_id`" \
          " AND `user`.`user_login_id`=`classifieds`.`user_id` AND `classifieds`.`agent_login_id`='" + str(session['lid']) + "'"
    res = db.select(qry)
    return render_template('agent/agent_accepted_classifieds_request.html', data=res)


# //GET method//
@app.route('/agent_view_payment_information')
def agent_view_payment_information():
    return render_template('agent/agent_view_payment_information.html')


@app.route('/agent_view_payment_information_search', methods=['post'])
def agent_view_payment_information_search():
    db = Db()
    opt = request.form['cont']
    if opt == "Magazine":
        qry = " SELECT `user`.`user_name`, `payments`.`amount`,`payments`.`date` FROM `agent`,`payments`,`user`,`magazine_requests`,`magazines` "\
              " WHERE `magazines`.`magazine_id`=`magazine_requests`.`magazine_id` AND `agent`.`agent_login_id`='"+str(session['lid'])+"'" \
              " AND `magazine_requests`.`agent_login_id`=`agent`.`agent_login_id`AND"\
              "`payments`.`user_id`=`user`.`user_login_id`AND `payments`.`request_id`=`magazine_requests`.magazine_request_id" \
              " AND  `payments`.`type`='Magazine'"
        res = db.select(qry)
    elif opt == "Newspaper":
        qry = " SELECT `user`.`user_name`,`payments`.`amount`,`payments`.`date` FROM `agent`,`payments`,`user`,`paper_requests`,`editions`"\
              " WHERE `paper_requests`.`edition_id`=`editions`.`edition_id` AND `agent`.`agent_login_id`='"+str(session['lid'])+"'" \
              " AND `paper_requests`.`agent_login_id`=`agent`.`agent_login_id`"\
              " AND `paper_requests`.`user_id`=`user`.`user_login_id` AND `payments`.`request_id`=`paper_requests`.`paper_request_id`" \
              " AND  `payments`.`type`='Newspaper'"
        res = db.select(qry)
    else:
        qry = "SELECT `user`.`user_name`,`payments`.`amount`,`payments`.`date` FROM `agent`,`payments`,`user`,`classifieds`"\
              " WHERE`payments`.`request_id`=`classifieds`.`classified_id` AND `agent`.`agent_login_id`='"+str(session['lid'])+"'" \
              " AND `classifieds`.`agent_login_id`=`agent`.`agent_login_id` AND"\
              " `payments`.`user_id`=`user`.`user_login_id` AND  `payments`.`type`='Classifieds'"
        res = db.select(qry)
    return render_template('agent/agent_view_payment_information.html',data=res)


# //GET method//
@app.route('/agent_view_complaint')
def agent_view_complaint():
    db = Db()
    qry = " SELECT complaints.*, `user`.* FROM complaints, `user` WHERE complaints.user_id = user.user_login_id" \
          " AND complaints.agent_login_id ='" + str(session['lid']) + "' AND replay='pending'"
    res = db.select(qry)
    return render_template('agent/agent_view_complaint.html', data=res)


# //GET method//
@app.route('/agent_sent_complaint_replay/<id>')
def agent_sent_complaint_replay(id):
    db = Db()
    session['complaint_id'] = id
    qry = "SELECT * FROM complaints WHERE complaint_id ='"+id+"'"
    res = db.selectOne(qry)
    return render_template('agent/agent_sent_complaint_replay.html', data=res)


# //EDIT POST method//
@app.route('/agent_sent_complaint_replay_post', methods=['POST'])
def agent_sent_complaint_replay_post():
    db = Db()
    x = session['complaint_id']
    replay = request.form['textfield2']
    qry = " UPDATE complaints SET replay = '"+replay+"' WHERE complaint_id = '"+str(x)+"'"
    res = db.update(qry)
    return "<script>window.location='/agent_view_complaint'; alert('Replay Sent Successfully...')</script>"


# //GET method//
@app.route('/agent_view_user_status')
def agent_view_user_status():
    db = Db()
    qry = " SELECT user_status.*, user.* FROM user_status, USER, agent" \
          " WHERE user_status.user_id = user.user_login_id AND agent.agent_login_id = '"+str(session['lid'])+"'"
    res = db.select(qry)
    return render_template('agent/agent_view_user_status.html', data=res)


# /GET method//
@app.route('/agent_add_paperboy')
def agent_add_paperboy():
    return render_template('agent/agent_add_paperboy.html')


# /POST method//
@app.route('/agent_add_paperboy_post', methods=['POST'])
def agent_add_paperboy_post():
    db = Db()
    name = request.form['textfield1']
    place = request.form['textfield2']
    number = request.form['textfield3']
    email = request.form['textfield4']
    photo = request.files['filefield1']
    photo.save("H:\\MY_SYSTEM_SOFTWARES_PROJECT\\FINAL_PROJECT_BCA_SURYA\\PAPER_APP_WEB\\static\\paperboy_image\\"+photo.filename)
    path = "/static/paperboy_image/"+photo.filename
    qry1 = " INSERT INTO login (user_name, PASSWORD, TYPE)" \
           " VALUES ('"+name+"','"+number+"','paperboy')"
    res1 = db.insert(qry1)
    qry = "INSERT INTO paperboy (agent_id, boy_name, boy_place, boy_contact_number, boy_email_id, boy_image, boy_login_id) " \
          " VALUES ('"+str(session['lid'])+"', '"+name+"', '"+place+"', '"+number+"', '"+email+"', '"+path+"', '"+str(res1)+"')"
    res = db.insert(qry)
    return 'Added Paperboy Successfully...'


# /GET method//
@app.route('/agent_view_paperboy')
def agent_view_paperboy():
    ab = Db()
    qry = "SELECT * FROM paperboy WHERE paperboy.agent_id = '"+str(session['lid'])+"'"
    res = ab.select(qry)
    return render_template('agent/agent_view_paperboy.html', data=res)


# // EDIT //
@app.route('/agent_view_paperboy_edit/<id>')
def agent_view_paperboy_edit(id):
    db = Db()
    session['boy_id'] = id
    qry = " SELECT * FROM paperboy WHERE agent_id = '"+str(session['lid'])+"' "
    res = db.selectOne(qry)
    return render_template("agent/agent_view_paperboy_edit.html", data=res)


# //EDIT POST method//
@app.route('/agent_view_paperboy_edit_post', methods=['POST'])
def agent_view_paperboy_edit_post():
    db = Db()
    a1 = session['boy_id']
    name = request.form['textfield1']
    place = request.form['textfield2']
    number = request.form['textfield3']
    email = request.form['textfield4']
    if "filefield1" in request.files:
        image = request.files['filefield1']
        if image.filename == "":
            qry = "UPDATE paperboy SET boy_name ='"+name+"', boy_place ='"+place+"', boy_contact_number ='"+number+"'," \
              " boy_email_id ='"+email+"' WHERE agent_id = '"+str(session['lid'])+"'"
            res = db.update(qry)
        else:
            image.save("H:\\MY_SYSTEM_SOFTWARES_PROJECT\\FINAL_PROJECT_BCA_SURYA\\PAPER_APP_WEB\\static\\paperboy_image\\"+image.filename)
            path = "/static/paperboy_image/"+image.filename
            qry1 = "UPDATE paperboy SET boy_name ='"+name+"', boy_place ='"+place+"', boy_contact_number ='"+number+"'," \
                   " boy_email_id ='"+email+"', boy_image ='"+path+"'" \
                   " WHERE agent_id = '"+str(session['lid'])+"'"
            res1 = db.update(qry1)
    else:
        qry2 = "UPDATE paperboy SET boy_name ='"+name+"', boy_place ='"+place+"', boy_contact_number ='"+number+"'," \
              " boy_email_id ='"+email+"' WHERE agent_id = '"+str(session['lid'])+"'"
        res2 = db.update(qry2)
    return 'Updated Successfully.'


# //DELETE//
@app.route('/agent_view_paperboy_delete/<id>')
def agent_view_paperboy_delete(id):
    db = Db()
    qry = "DELETE FROM paperboy WHERE boy_id = '"+id+"'"
    db.delete(qry)
    return "<script>window.location='/agent_view_paperboy'; alert('Paperbboy Deleted Successfully.')</script>"


# //GET method//
@app.route('/agent_send_allocated_list')
def agent_send_allocated_list():
    return render_template('agent/agent_send_allocated_list.html')


# //POST method//
@app.route('/agent_send_allocated_list_post', methods=['POST'])
def agent_send_allocated_list_post():
    ab = Db()
    prod = request.form['product']
    session['typeofreq']=prod
    if prod == "Magazine":
        qry = " SELECT USER.*, magazine_requests.*, subscriber.*, provider.provider_name, magazines.* " \
              " FROM USER, magazine_requests, subscriber, provider, `magazines` " \
              " WHERE subscriber.agent_id = '"+str(session['lid'])+"' " \
              " AND subscriber.user_id = user.user_login_id " \
              " AND subscriber.user_id = magazine_requests.user_id AND magazine_requests.status = 'Approved' " \
              " AND `magazine_requests`.`magazine_id` = `magazines`.magazine_id " \
              " AND magazines.provider_login_id = provider.`provider_login_id`"
        res = ab.select(qry)
    else:
        qry = " SELECT USER.*, `paper_requests`.*, subscriber.*, provider.provider_name, editions.*, `edition_languages`.* " \
              "FROM USER, `paper_requests`, subscriber, provider, editions, `edition_languages`" \
              " WHERE subscriber.agent_id = '"+str(session['lid'])+"' AND subscriber.user_id = user.user_login_id" \
              " AND subscriber.user_id = paper_requests.user_id AND paper_requests.status = 'Approved'" \
              " AND `paper_requests`.`edition_id` = `editions`.edition_id AND editions.provider_login_id = provider.`provider_login_id`" \
              " AND `edition_languages`.edition_id = editions.edition_id"
        res = ab.select(qry)
    return render_template('agent/agent_send_allocated_list.html', data=res,ty=prod)


# //GET method/ Allocate//
@app.route('/agent_send_allocated_list_allocate/<id>/<prp>')
def agent_send_allocated_list_allocate(id,prp):
    db = Db()
    session['subrid'] = id
    session['rid']=prp
    qry1 = "SELECT * FROM paperboy WHERE agent_id = '"+str(session['lid'])+"' "
    res1 = db.select(qry1)
    qry2 = "select `user`.* from `user`,`subscriber` where `subscriber`.`user_id`=`user`.`user_login_id` and `subscriber`.`subscriber_id`='"+id+"'"
    res2 = db.selectOne(qry2)
    return render_template("agent/agent_send_allocated_list_allocate.html", data1=res1, data=res2)


@app.route('/agent_send_allocated_list_allocate2/<id>/<prp>')
def agent_send_allocated_list_allocate2(id,prp):
    db = Db()
    session['subrid'] = id
    session['rid']=prp
    qry1 = "SELECT * FROM paperboy WHERE agent_id = '"+str(session['lid'])+"' "
    res1 = db.select(qry1)
    qry2 = "select `user`.* from `user`,`subscriber` where `subscriber`.`user_id`=`user`.`user_login_id` and `subscriber`.`subscriber_id`='"+id+"'"
    res2 = db.selectOne(qry2)
    return render_template("agent/agent_send_allocated_list_allocate.html", data1=res1, data=res2)


# //POST method/ Allocate//
@app.route('/agent_send_allocated_list_allocate_post', methods=['POST'])
def agent_send_allocated_list_allocate_post():
    db = Db()
    boyid = request.form['boy']
    reqid=session['rid']
    x=session['typeofreq']
    qry = " INSERT INTO allocate_paperboy (request_id, boy_id, TYPE, allocated_date, status)" \
          " VALUES ('"+reqid+"', '"+boyid+"', '"+str(x)+"', curdate(), 'Allocated')"
    res = db.insert(qry)
    return "<script>alert('Allocated Successfully...'); window.location='/agent_send_allocated_list'</script>"


# /GET method//
@app.route('/agent_view_allocation_status')
def agent_view_allocation_status():
    return render_template('agent/agent_view_allocation_status.html')


# /GET method//
@app.route('/agent_view_allocation_status_post',methods=['post'])
def agent_view_allocation_status_post():
    db = Db()
    opt = request.form['select']
    if opt == "Magazine":
        qry = " SELECT paperboy.*, allocate_paperboy.*, user.*, subscriber.*, magazine_requests.*" \
              " FROM paperboy, allocate_paperboy, USER, subscriber, magazine_requests" \
              " WHERE allocate_paperboy.type = 'Magazine' AND allocate_paperboy.boy_id = paperboy.boy_login_id" \
              " AND allocate_paperboy.status = 'Allocated' AND allocate_paperboy.request_id = magazine_requests.magazine_request_id" \
              " AND magazine_requests.user_id = user.user_login_id AND magazine_requests.agent_login_id = '"+str(session['lid'])+"'" \
              " AND subscriber.agent_id = '"+str(session['lid'])+"'"
        res = db.select(qry)
    else:
        qry = "SELECT user.user_name, `allocate_paperboy`.*, `paperboy`.*" \
              " FROM `user`, allocate_paperboy, paperboy, agent" \
              " WHERE `agent`.`agent_login_id`='"+str(session['lid'])+"' AND allocate_paperboy.type ='Newspaper'"
        res = db.select(qry)
    return render_template('agent/agent_view_allocation_status.html', data=res)


# //GET method//
@app.route('/agent_change_username_password')
def agent_change_username_password():
    return render_template('agent/agent_change_username_password.html')


# //post method//
@app.route('/agent_change_username_password_post', methods=['POST'])
def agent_change_username_password_post():
    db = Db()
    username1 = request.form['textfield1']
    password1 = request.form['textfield2']
    username2 = request.form['textfield3']
    password2 = request.form['textfield4']
    qry1 = "SELECT * FROM login WHERE user_name ='"+username1+"' AND password ='"+password1+"' AND login_id ='"+str(session['lid'])+"' "
    res1 = db.selectOne(qry1)
    if res1 is not None:
        qry2 = " UPDATE login SET user_name ='"+username2+"', password ='"+password2+"'" \
               " WHERE login_id ='"+str(session['lid'])+"' "
        res2 = db.update(qry2)
    else:
        return"<script> alert('Invalid Username or Password');" \
              " window.location='/agent_change_username_password' </script>"
    return 'Username/Password Changed Successfully.'


#######################################################################################################################
#######################################################################################################################
# //USER/SUBSCRIBER MODULE//ANDROID PORTION//

# //ANDROID POST method//
@app.route('/and_login_post', methods=['POST'])
def and_login_post():
    username = request.form['uname']
    password = request.form['pswd']
    db = Db()
    qry = "SELECT * FROM login WHERE user_name='"+username+"' AND `password`='"+password+"'"
    res = db.selectOne(qry)
    print(qry)
    print(res)
    if res is not None:
        return jsonify(status='ok', lid=res['login_id'], type=res['type'])
    else:
        return jsonify(status='no')


# //ANDROID POST method//
@app.route('/and_user_registration', methods=['POST'])
def and_user_registration():
    db = Db()
    name = request.form['name']
    gender = request.form['gender']
    place = request.form['place']
    post = request.form['post']
    district = request.form['district']
    pin = request.form['pin']
    email = request.form['email']
    number = request.form['number']
    image = request.files['pic']
    pas = request.form['password']
    # a = base64.b64decode(image)
    dt = datetime.datetime.now()
    dd = str(dt).replace(" ", "_").replace(":", "_").replace("-", "_")
    image.save("H:\\MY_SYSTEM_SOFTWARES_PROJECT\\FINAL_PROJECT_BCA_SURYA\\PAPER_APP_WEB\\static\\user_image\\"+dd+".jpg")
    path = "/static/user_image/" + dd + ".jpg"
    # fh.write(a)
    # fh.close()
    res = db.insert("insert into login(user_name,password,type)values('"+email+"','"+pas+"','user')")
    qry = "INSERT INTO USER (user_name, gender, place, post, district, pin, email_id, contact_number, user_image,user_login_id)" \
          " VALUES ('"+name+"', '"+gender+"', '"+place+"', '"+post+"', '"+district+"', '"+pin+"', '"+email+"', '"+number+"', '"+path+"','"+str(res)+"')"
    res = db.insert(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_view_agent', methods=['POST'])
def user_view_agent():
    db = Db()
    lid = request.form['lid']
    qry = "SELECT `agent`.*,`subscriber`.`status` FROM `agent`,`subscriber` "\
        "WHERE `agent`.`agent_login_id`=`subscriber`.`agent_id` AND `subscriber`.`user_id`='"+lid+"'"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_view_agent_by_district', methods=['POST'])
def user_view_agent_by_district():
    db = Db()
    name = request.form['dist']
    lid = request.form['lid']
    qry = "SELECT `agent`.* FROM `agent` WHERE  `agent`.agent_district like'%"+name+"%'"
    res = db.select(qry)
    return jsonify(status='ok', data=res)


# //ANDROID POST method//
@app.route('/user_sent_subscriber_request',methods=['post'])
def user_sent_subscriber_request():
    lid = request.form['lid']
    agent_id=request.form['agent_id']
    db=Db()
    res=db.selectOne("select * from subscriber where user_id='"+str(lid)+"' and agent_id='"+str(agent_id)+"'")
    if res is not None:
        if res['status'] == "accepted":
            return jsonify(status="ok")
        elif res['status'] == "pending":
            return jsonify(status="no")
        else:
            db.update("update subscriber set status='pending' where subscriber_id='"+str(res['subscriber_id'])+"'")
            return jsonify(status="1")
    else:
        db.insert("insert into subscriber(user_id,agent_id,status)values('"+str(lid)+"','"+str(agent_id)+"','pending')")
        return jsonify(status="ok")


# //ANDROID POST method//
@app.route('/user_view_subscriber_status',methods=['post'])
def user_view_subscriber_status():
    lid = request.form['lid']
    print(lid)
    db = Db()
    res = db.select("select agent.*,subscriber.status from agent,subscriber where subscriber.user_id='"+lid+"' and subscriber.agent_id=agent.agent_login_id")
    print(res)
    if res is not None:
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")


# //ANDROID POST method//
@app.route('/user_sent_paper_request', methods=['POST'])
def user_sent_paper_request():
    db = Db()
    lid = request.form['lid']
    agent = request.form['agent_id']
    edn = request.form['edition_id']
    ednlan = request.form['edition_language_id']
    qry = " INSERT INTO paper_requests (user_id, edition_id, `edition_language_id`, `request_date`, `status`, `agent_login_id`)" \
          " VALUES ('"+lid+"', '"+edn+"', '"+ednlan+"', curdate(), 'pending', '"+agent+"' )"
    res = db.insert(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_view_magazine_requests', methods=['POST'])
def user_view_magazine_requests():
    db = Db()
    lid = request.form['lid']
    agent_id=request.form['agent_id']
    qry = "SELECT *,`magazine_requests`.`magazine_id` AS m_id FROM `magazine_requests`,`magazines`,`provider` WHERE "\
        "`magazine_requests`.`magazine_id`=`magazines`.`magazine_id` AND `magazine_requests`.`agent_login_id`='"+agent_id+"' AND"\
        " `magazine_requests`.`user_id`='"+lid+"' AND `magazines`.`provider_login_id`=`provider`.`provider_login_id`"
    res = db.select(qry)
    print(res)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/and_view_provider', methods=['post'])
def and_view_provider():
    db = Db()
    res = db.select("SELECT * FROM `provider`")
    return jsonify(status="ok", data=res)


# //ANDROID POST method//
@app.route('/user_view_magazine', methods=['post'])
def user_view_magazine():
    db = Db()
    res = db.select(" SELECT * FROM `magazines`,`provider`, login WHERE `magazines`.`provider_login_id`=`provider`.`provider_login_id`"
                    " AND `provider`.`provider_login_id`= login.login_id AND login.type = 'provider'")
    return jsonify(status="ok", data=res)


# //ANDROID POST method//
@app.route('/user_view_magazine_extra', methods=['post'])
def user_view_magazine_extra():
    db = Db()
    providr_id = request.form['provider_login_id']
    lang = request.form['lang']
    res = db.select(" SELECT * FROM `magazines`,`provider` WHERE `magazines`.`provider_login_id`=`provider`.`provider_login_id`"
                    " AND `provider`.`provider_login_id`='"+providr_id+"' AND `magazines`.`language`='"+lang+"'")
    return jsonify(status="ok", data=res)


# //ANDROID POST method//
@app.route('/user_sent_request', methods=['POST'])
def user_sent_request():
    db = Db()
    typ = request.form['type']
    accno = request.form['accno']
    pin = request.form['pin']
    amount = request.form['amount']
    res1 = db.selectOne("SELECT * FROM `bank` WHERE `account_no`='"+accno+"' AND `password`='"+pin+"' AND `amount`>'"+amount+"'")
    if res1 is not None:
        if typ == "Magazine":
            lid = request.form['lid']
            agent = request.form['agent_id']
            mag = request.form['mag_id']
            res2=db.selectOne("SELECT * FROM `magazine_requests` WHERE `magazine_id`='"+mag+"' AND `user_id`='"+lid+"' AND `agent_login_id`='"+agent+"'")
            if res2 is None:
                qry = "INSERT INTO `magazine_requests` (user_id, `magazine_id`, `request_date`, `status`, agent_login_id)" \
                " VALUES ('"+lid+"', '"+mag+"', curdate(), 'pending', '"+agent+"')"
                res = db.insert(qry)
                qry1="INSERT INTO `payments`(`user_id`,`type`,`amount`,`date`,`request_id`) VALUES('"+str(lid)+"','"+typ+"','"+str(amount)+"',curdate(),'"+str(res)+"')"
                db.insert(qry1)
                amt = int(res1['amount'])-int(amount)
                db.update("update bank set amount='"+str(amt)+"' where bank_id='"+str(res1['bank_id'])+"'")
            else:
                return jsonify(status="no")
        elif typ == "Newspaper":
            edition_id = request.form['edition_id']
            lid = request.form['lid']
            agent_id = request.form['agent_id']
            edition_language_id = request.form['edition_language_id']
            qry = " SELECT * FROM `paper_requests` WHERE `user_id`='"+lid+"' AND `edition_id`='"+edition_id+"' AND "\
                  "`edition_language_id`='"+edition_language_id+"' AND `agent_login_id`='"+agent_id+"' AND `status`!='rejected'"
            res3 = db.selectOne(qry)
            if res3 is None:
                qry3 = " INSERT INTO `paper_requests`(`user_id`,`edition_id`,`edition_language_id`,`request_date`,`status`,`agent_login_id`)"\
                       " VALUES('"+lid+"','"+edition_id+"','"+edition_language_id+"', curdate(), 'pending', '"+agent_id+"')"
                res = db.insert(qry3)
                qry1 = " INSERT INTO `payments`(`user_id`,`type`,`amount`,`date`,`request_id`) " \
                       " VALUES('"+str(lid)+"','"+typ+"','"+str(amount)+"',curdate(),'"+str(res)+"')"
                db.insert(qry1)
                amt = int(res1['amount'])-int(amount)
                db.update("update bank set amount='"+str(amt)+"' where bank_id='"+str(res1['bank_id'])+"'")
            else:
                return jsonify(status="no")
        return jsonify(status="ok")
    else:
        return jsonify(status="n")


import time, datetime
from encodings.base64_codec import base64_decode
import base64


# //ANDROID POST method//
@app.route('/user_add_classifieds', methods=['POST'])
def user_add_classifieds():
    db = Db()
    lid = request.form['lid']
    image = request.files['pic']
    provider = request.form['provider']
    agent_id = request.form['agent_id']
    description = request.form['desc']
    accno = request.form['accno']
    pin = request.form['pin']
    amount = 500
    dt = datetime.datetime.now()
    dd = str(dt).replace(" ", "_").replace(":", "_").replace("-", "_")
    image.save("D:\\Riss\\Project_2021-22\\ignou-news\\PAPER_APP_WEB\\static\\classifieds\\"+dd+".jpg")
    # fh = open("C:\\Users\\USER\\PycharmProjects\\newspaper\\static\\user_image\\" + dd + ".jpg", "wb")
    path = "/static/classifieds/" + dd + ".jpg"
    res1 = db.selectOne("SELECT * FROM `bank` WHERE `account_no`='"+accno+"' AND `password`='"+pin+"' AND `amount`>'"+str(amount)+"'")
    if res1 is not None:
        qry = " INSERT INTO `classifieds`(`user_id`,`agent_login_id`,`provider_login_id`,`news_file`,`description`,`date`,`status`,`amount`)"\
              " VALUES('"+str(lid)+"','"+str(agent_id)+"','"+provider+"','"+path+"=','"+description+"',curdate(),'pending','500')"
        res = db.insert(qry)
        amt = int(res1['amount'])-amount
        qry1 = "INSERT INTO `payments`(`user_id`,`type`,`amount`,`date`,`request_id`) VALUES('"+str(lid)+"','Classifieds','500',curdate(),'"+str(res)+"')"
        db.insert(qry1)
        amt = int(res1['amount'])-int(amount)
        db.update("update bank set amount='"+str(amt)+"' where bank_id='"+str(res1['bank_id'])+"'")
        return jsonify(status="ok")
    else:
        return jsonify(status="no")


# //ANDROID POST method//
@app.route('/user_remove_magazine_request', methods=['POST'])
def user_remove_magazine_request():
    db = Db()
    mag_request_id=request.form['mag_req_id']
    qry = "DELETE FROM magazine_requests WHERE magazine_request_id ='"+mag_request_id+"'"
    res = db.delete(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_view_magazine_status', methods=['POST'])
def user_view_magazine_status():
    db = Db()
    lid = request.form['lid']
    qry = "SELECT * FROM magazine_requests WHERE user_id ='"+lid+"'"
    res = db.select(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_view_newspaper_request', methods=['POST'])
def user_view_newspaper_request():
    db = Db()
    lid = request.form['lid']
    agent_id=request.form['agent_id']
    qry = " SELECT *,editions.edition_place as place FROM `paper_requests`,`editions`,`edition_languages` WHERE `paper_requests`.`edition_id`=`editions`.`edition_id`" \
          " AND `paper_requests`.`edition_language_id`=`edition_languages`.`language_id`" \
          " AND `editions`.`edition_id`=`edition_languages`.`edition_id` AND `paper_requests`.`user_id`='"+lid+"'" \
          " AND `paper_requests`.`agent_login_id`='"+agent_id+"'"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_view_edition_language', methods=['POST'])
def user_view_edition_language():
    db = Db()
    qry = "SELECT * FROM `edition_languages`"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_view_editions', methods=['POST'])
def user_view_editions():
    db = Db()
    qry = " SELECT `editions`.*,editions.edition_place as place,`edition_languages`.`language_id`,`edition_languages`.`edition_language`,`provider`.`provider_name`,`price`.`price`" \
          " FROM `edition_languages`,`editions`,`price`,`provider` WHERE `editions`.`edition_id`=`edition_languages`.`edition_id`" \
          " AND `editions`.`provider_login_id`=`provider`.`provider_login_id` AND `editions`.`edition_id`=`price`.`edition_id`"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_view_edition_extra', methods=['POST'])
def user_view_edition_extra():
    db = Db()
    lan = request.form['lang']
    proid = request.form['provider_login_id']
    qry = " SELECT `editions`.*,editions.edition_place as place,`edition_languages`.`language_id`,`edition_languages`.`edition_language`,`provider`.`provider_name`,`price`.`price`" \
          " FROM `edition_languages`,`editions`,`price`,`provider` WHERE `editions`.`edition_id`=`edition_languages`.`edition_id`" \
          " AND `editions`.`provider_login_id`=`provider`.`provider_login_id` AND `editions`.`edition_id`=`price`.`edition_id`" \
          " AND `editions`.`provider_login_id`='"+proid+"' AND `edition_languages`.`language_id`='"+lan+"' and  price.language_id=edition_languages.language_id"
    res = db.select(qry)
    print(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_sent_classifieds_request', methods=['POST'])
def user_sent_classifieds_request():
    db = Db()
    lid = request.form['lid']
    agent = request.form['alid']
    pro = request.form['prov']
    nfile = request.form['nf']
    des = request.form['desc']
    qry = "INSERT INTO classifieds (`user_id`, `agent_login_id`, `provider_login_id`, `news_file`, `description`, `date`, `status`)" \
          " VALUES ('"+lid+"', '"+agent+"', '"+pro+"', '"+nfile+"', '"+des+"', curdate(), 'pending')"
    res = db.insert(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_view_classifieds_status', methods=['POST'])
def user_view_classifieds_status():
    db = Db()
    lid = request.form['lid']
    qry = "SELECT * FROM `classifieds` WHERE user_id ='"+lid+"'"
    res = db.select(qry)
    return jsonify(status='ok')


# //ANDROID POST method//                   [Complaint.java]
@app.route('/user_sent_complaint', methods=['POST'])
def user_sent_complaint():
    db = Db()
    lid = request.form['lid']
    agent = request.form['agent_id']
    complaint = request.form['com']
    qry = "INSERT INTO complaints (user_id, DATE, complaint, agent_login_id) VALUES ('"+lid+"', curdate(), '"+complaint+"', '"+agent+"')"
    res = db.insert(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_view_complaint_replay', methods=['POST'])
def user_view_complaint_replay():
    db = Db()
    lid = request.form['lid']
    agentid = request.form['agent_id']
    qry = "SELECT * FROM complaints WHERE user_id ='"+lid+"' and agent_login_id='"+agentid+"'"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_view_notification', methods=['POST'])
def user_view_notification():
    db = Db()
    qry = "SELECT notifications.*, provider.* FROM notifications, provider WHERE notifications.provider_login_id = provider.provider_login_id"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_current_status', methods=['POST'])
def user_current_status():
    db = Db()
    lid = request.form['lid']
    status = request.form['stat']
    from_date=request.form['from_date']
    to_date=request.form['to_date']
    qry1 = "SELECT * FROM `user_status` WHERE `user_id` = '"+lid+"'"
    res1 = db.selectOne(qry1)
    if qry1 is not None:
        qry2 = "UPDATE `user_status` SET `status` ='"+status+"', from_date='"+from_date+"',to_date='"+to_date+"' WHERE user_id ='"+lid+"'"
        res2 = db.update(qry2)
    else:
        qry3 = "INSERT INTO `user_status` (user_id, status, from_date,to_date ) VALUES ('"+lid+"', '"+status+"','"+from_date+"','"+to_date+"')"
        res3 = db.insert(qry3)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_make_payment', methods=['POST'])
def user_make_payment():
    db = Db()
    lid = request.form['lid']
    accno = request.form['acn']
    pwd = request.form['pw']
    qry = "SELECT * FROM bank WHERE `account_no` = AND `password` = "
    res = db.select(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/user_view_payment_history', methods=['POST'])
def user_view_payment_history():
    db = Db()
    lid = request.form['lid']
    qry = "SELECT * FROM payments WHERE user_id ='"+lid+"'"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_view_classified_requests', methods=['POST'])
def user_view_classified_requests():
    db = Db()
    lid = request.form['lid']
    agent_id=request.form['agent_id']
    qry = "SELECT * FROM `classifieds`,`provider` WHERE `classifieds`.`provider_login_id`=`provider`.`provider_login_id` AND `classifieds`.`user_id`='"+lid+"' AND `classifieds`.`agent_login_id`='"+agent_id+"'"
    res = db.select(qry)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/user_view_my_stat', methods=['post'])
def user_view_my_stat():
    lid = request.form['lid']
    db = Db()
    res = db.selectOne("select * from user_status where user_id='"+lid+"'")
    if res is not None:
        return jsonify(status="ok", stat=res['status'], fro=res['from_date'], to=res['to_date'])
    else:
        return jsonify(status="no")


#######################################################################################################################
#######################################################################################################################
# //PAPERBOY MODULE//ANDROID PORTION//

# //ANDROID POST method//
@app.route('/paperboy_profile_management',methods=['POST'])
def paperboy_profile_management():
    bb = Db()
    name = request.form['n']
    place = request.form['p']
    phone = request.form['ph']
    email = request.form['e']
    image = request.form['image']
    if image == "aa":
        qry1 = " UPDATE paperboy SET `boy_name`= '"+name+"', `boy_place`='"+place+"', `boy_contact_number`='"+phone+"'," \
               " `boy_email_id`='"+email+"' WHERE provider_login_id ='"+str(session['lid'])+'"'
        res1 = bb.update(qry1)
    else:
            a = base64.b64decode(image)
            dt = datetime.datetime.now()
            dd = str(dt).replace(" ", "_").replace(":", "_").replace("-", "_")
            fh = open("C:\\Users\\USER\\PycharmProjects\\newspaper\\static\\paperboy_image\\" + dd + ".jpg", "wb")
            path = "/static/paperboy_image/" + dd + ".jpg"
            fh.write(a)
            fh.close()
            qry2 = " UPDATE paperboy SET `boy_name`= '"+name+"', `boy_place`='"+place+"', `boy_contact_number`='"+phone+"'," \
                   " `boy_email_id`='"+email+"', boy_image ='"+path+"'  WHERE provider_login_id ='"+str(session['lid'])+'"'
            res2 = bb.update(qry2)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/paperboy_view_paper_allocation_list', methods=['POST'])
def paperboy_view_paper_allocation_list():
    db = Db()
    lid = request.form['lid']
    qry = "SELECT allocate_paperboy.*,allocate_paperboy.status as dst, paper_requests.`paper_request_id`,`paper_requests`.`user_id`,`paper_requests`.`edition_id`,`paper_requests`.`edition_language_id`,`paper_requests`.`request_date` AS DATE,`paper_requests`.`status`,`paper_requests`.`start_date`,`paper_requests`.`agent_login_id`, user.*, editions.*, edition_languages.*, provider.*" \
          " FROM `allocate_paperboy`, `paper_requests`, `user`, `editions`, `edition_languages`, `provider`" \
          " WHERE allocate_paperboy.boy_id = '"+lid+"'" \
          " AND allocate_paperboy.request_id = paper_requests.paper_request_id" \
          " AND paper_requests.edition_id = editions.edition_id" \
          " AND paper_requests.edition_language_id = edition_languages.language_id" \
          " AND paper_requests.user_id = user.user_login_id" \
          " AND editions.provider_login_id = provider.provider_login_id" \
          " AND allocate_paperboy.allocated_date = CURDATE()"
    res = db.select(qry)
    print(qry)
    print(res)
    return jsonify(status='ok',data=res)


# //ANDROID POST method//
@app.route('/paperboy_view_magazine_allocation_list', methods=['POST'])
def paperboy_view_magazine_allocation_list():
    db = Db()
    lid = request.form['lid']
    qry = "SELECT allocate_paperboy.*, `magazine_requests`.*, `magazines`.*,  user.*, provider.*" \
          " FROM `allocate_paperboy`, `magazine_requests`, magazines, `user`, `provider`" \
          " WHERE allocate_paperboy.boy_id = '"+lid+"'" \
          " AND allocate_paperboy.request_id = magazine_requests.magazine_request_id" \
          " AND allocate_paperboy.type ='Magazine'" \
          " AND magazine_requests.magazine_id = magazines.magazine_id" \
          " AND magazine_requests.user_id = user.user_login_id" \
          " AND magazines.provider_login_id = provider.provider_login_id" \
          " AND allocate_paperboy.allocated_date = CURDATE()"
    res = db.select(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/paperboy_view_user_status', methods=['POST'])
def paperboy_view_user_status():
    db = Db()
    lid = request.form['lid']
    qry = "SELECT user_status.* FROM `user_status` WHERE boy_id ='"+lid+"'"
    res = db.select(qry)
    return jsonify(status='ok')


# //ANDROID POST method//
@app.route('/and_paper_boy_view_profile', methods=['POST'])
def and_paper_boy_view_profile():
    db = Db()
    lid = request.form['lid']
    qry = " SELECT * FROM `paperboy` WHERE `boy_login_id`='"+lid+"'"
    res = db.selectOne(qry)
    return jsonify(status='ok',boy_name=res['boy_name'],boy_place=res['boy_place'],boy_contact_no=res['boy_contact_number'],boy_email_id=res['boy_email_id'],boy_image=res['boy_image'])


# //ANDROID POST method//
@app.route('/paperboy_paper_status', methods=['POST'])
def user_status():
    db = Db()
    lid = request.form['lid']
    papstat = request.form['psi']
    status = request.form['sts']
    qry1 = "SELECT * FROM `paper_status` WHERE date = curdate() AND `paperboy_id` = '"+lid+"'"
    res1 = db.selectOne(qry1)
    if qry1 is not None:
        qry2 = "UPDATE `paper_status` SET `status` ='"+status+"' WHERE paperboy_id ='"+id+"' AND paper_status_id ='"+papstat+"'"
        res2 = db.update(qry2)
    else:
        qry3 = "INSERT INTO `paper_status` (paper_status_id, paperboy_id, status ) VALUES ('"+papstat+"', '"+lid+"', '"+status+"')"
        res3 = db.insert(qry3)
    return jsonify(status='ok')



@app.route('/paper_boy_delivered_alocation', methods=['POST'])
def paper_boy_delivered_alocation():
    db = Db()
    aloc_id = request.form['aloc_id']
    qry ="update allocate_paperboy set status='Delivered' where allocation_id='"+aloc_id+"'"
    res = db.update(qry)
    return jsonify(status='ok')


@app.route('/paper_boy_not_delivered_alocation', methods=['POST'])
def paper_boy_not_delivered_alocation():
    db = Db()
    aloc_id = request.form['aloc_id']
    qry ="update allocate_paperboy set status='UnDelivered' where allocation_id='"+aloc_id+"'"
    res = db.update(qry)
    return jsonify(status='ok')

#######################################################################################################################
#######################################################################################################################

if __name__ == '__main__':
    app.run(debug=True,host="0.0.0.0",port=5000)

#######################################################################################################################
#######################################################################################################################
